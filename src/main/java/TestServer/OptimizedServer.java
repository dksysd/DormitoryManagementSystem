package TestServer;

import server.controller.AuthController;
import server.network.protocol.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class OptimizedServer {
    private final int port;
    private final int workerThreads;
    private final ExecutorService workExecutor;
    private final BlockingQueue<WorkItem> workQueue;
    private final Router router;

    public OptimizedServer() {
        System.out.println("Initializing Optimized Server");
        System.out.println("Initializing Configs...");
        Config config = Config.getInstance();
        port = Integer.parseInt(config.get("PORT").replaceAll("_", ""));
        workerThreads = Integer.parseInt(config.get("WORKER_THREADS").replaceAll("_", ""));

        System.out.println("Initializing WorkerThreads...");
        workExecutor = Executors.newFixedThreadPool(workerThreads);
        workQueue = new LinkedBlockingQueue<>();

        System.out.println("Initializing Routers...");
        router = new Router() {
            @Override
            void init() {
                addRouter(RequestProtocol.Method.POST, "/login", AuthController::login);
            }
        };
    }

    public void start() {
        System.out.println("Starting Optimized Server");
        System.out.println("Starting WorkerThreads...");
        for (int i = 0; i < workerThreads; i++) {
            startWorker();
        }

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));) {
            System.out.println("Server started on port " + port);

            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel client, Void attachment) {
                    server.accept(null, this);
                    try {
                        System.out.println("Server accepted connection" + client.getRemoteAddress().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handleClient(client);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    exc.printStackTrace();
                }
            });

            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(AsynchronousSocketChannel client) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        client.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                if (result == -1) {
                    closeClient(client);
                    return;
                }

                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                if (!workQueue.add(new WorkItem(client, bytes, System.currentTimeMillis()))) {
                    System.out.println("Fail to add request to queue.");
                }

                buffer.clear();
                client.read(buffer, null, this);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
                closeClient(client);
            }
        });
    }

    private void closeClient(AsynchronousSocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startWorker() {
        workExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    WorkItem workItem = workQueue.take();
                    work(workItem);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void work(WorkItem workItem) {
        RequestProtocol requestProtocol;
        try {
            requestProtocol = ProtocolSerializable.deserialize(workItem.data, RequestProtocol.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ResponseProtocol responseProtocol;
        try {
            isValidProtocolData(workItem.data, requestProtocol);
            responseProtocol = new ResponseProtocol();
            router.route(requestProtocol, responseProtocol);
        } catch (IllegalProtocolException e) {
            responseProtocol = new ResponseProtocol();
            responseProtocol.setStatus(ResponseProtocol.Status.InternalServerError);
            responseProtocol.getHeader().setContentType(Header.ContentType.APPLICATION_JSON);
            responseProtocol.getBody().addData("error", e.getMessage());
        } catch (Exception e) {
            responseProtocol = new ResponseProtocol();
            responseProtocol.setStatus(ResponseProtocol.Status.InternalServerError);
            responseProtocol.getHeader().setContentType(Header.ContentType.APPLICATION_JSON);
        }

        response(workItem, requestProtocol, responseProtocol);
    }

    private void response(WorkItem workItem, RequestProtocol requestProtocol, ResponseProtocol responseProtocol) {
        try {
            byte[] responseData = ProtocolSerializable.serialize(responseProtocol);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
            workItem.client.write(responseBuffer, null, new CompletionHandler<Integer, Void>() {

                @Override
                public void completed(Integer result, Void attachment) {
                    if(responseBuffer.hasRemaining()) {
                        workItem.client.write(responseBuffer, null, this);
                    } else {
                        StringBuilder log = new StringBuilder();
                        String address;
                        try {
                            address = workItem.client.getRemoteAddress().toString();
                        } catch (IOException e) {
                            address = "UNKNOWN";
                        }

                        log.append("(").append(address).append(") ").append(requestProtocol.getMethod()).append(" - ").append("\"").append(requestProtocol.getUrl()).append("\"").append("[").append(System.currentTimeMillis() - workItem.timestamp).append("ms]");
                        System.out.println(log);
                    }
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    exc.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isValidProtocolData(byte[] data, Protocol protocol) throws IllegalProtocolException {
        int startLineDelimiterIndex = findDelimiter(data, 0);
        int headerDelimiterIndex = findDelimiter(data, startLineDelimiterIndex + Protocol.DelimiterBytes.length);
        int actualBodyLength = data.length - headerDelimiterIndex - Protocol.DelimiterBytes.length;
        if (protocol.getHeader().getBodyLength() != actualBodyLength) {
            throw new IllegalProtocolException("Body length mismatch [expected:" + protocol.getHeader().getBodyLength() + ", actual:" + actualBodyLength + "]");
        }
    }

    private int findDelimiter(byte[] data, int pos) {
        int maxLength = data.length - Protocol.DelimiterBytes.length;
        for (int i = pos; i < maxLength; i++) {
            boolean isFound = true;
            for (int j = 0; j < Protocol.DelimiterBytes.length; j++) {
                if (data[i + j] != Protocol.DelimiterBytes[j]) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return i;
            }
        }
        return -1;
    }


    private record WorkItem(AsynchronousSocketChannel client, byte[] data, long timestamp) {
    }

    public static void main(String[] args) {
        new OptimizedServer().start();
    }
}
