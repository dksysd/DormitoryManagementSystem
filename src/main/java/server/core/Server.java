package server.core;

import lombok.Getter;
import server.config.Config;
import server.config.RequestHandlerInitializer;
import server.core.handler.AcceptHandler;
import server.core.handler.OutputHandler;
import server.core.handler.RequestHandler;
import server.core.persistence.Session;
import server.core.persistence.WorkItem;
import shared.protocol.persistence.*;
import shared.protocol.util.ProtocolSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class Server {
    @Getter
    public static final Server INSTANCE = new Server();

    private final int port;
    private final int workerThreads;
    private final ExecutorService workerExecutor;
    private final BlockingQueue<WorkItem> workQueue;
    private final RequestHandler requestHandler;

    private Server() {
        port = Config.getInt("PORT");
        workerThreads = Config.getInt("WORKER_THREADS");
        int workQueueCapacity = Config.getInt("QUEUE_CAPACITY");

        this.workerExecutor = Executors.newFixedThreadPool(workerThreads);
        this.workQueue = new LinkedBlockingQueue<>(workQueueCapacity);
        this.requestHandler = RequestHandler.getINSTANCE();

        for (int i = 0; i < workerThreads; i++) {
            workerExecutor.submit(() -> {}); // 빈 작업 제출
        }
    }

    public void start() {
        startWorker();

        RequestHandlerInitializer.init(this.requestHandler);

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port), 10_000)) {
            server.accept(server, new AcceptHandler(this::closeClient, workQueue));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startWorker() {
        for (int i = 0; i < workerThreads; i++) {
            workerExecutor.submit(() -> {
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
    }

    private void work(WorkItem workItem) {
        // todo session 기능 만들기
        checkSessionId(workItem.getRequestProtocol());
        System.out.println("Working " + workItem);
        Protocol<?> protocol;
        try {
            protocol = requestHandler.request(workItem.getRequestProtocol());
        } catch (Exception e) {
            protocol = createErrorProtocol();
            e.printStackTrace();
        }
        workItem.setResponseProtocol(protocol);
        ByteBuffer buffer = ProtocolSerializer.serialize(protocol);
        buffer.flip();
        workItem.getClient().write(buffer, buffer, new OutputHandler(workItem, this::closeClient));
    }

    private void checkSessionId(Protocol<?> protocol) {
        boolean hasSessionId = protocol.getChildren().stream().anyMatch(child -> child.getHeader().getCode() == Code.ValueCode.SESSION_ID);
        if (!hasSessionId) {
            Session session = SessionManager.getINSTANCE().createSession();

            Protocol<String> sessionValueProtocol = new Protocol<>();
            Header header = new Header();
            header.setType(Type.VALUE);
            header.setCode(Code.ValueCode.SESSION_ID);
            header.setDataType(DataType.STRING);
            sessionValueProtocol.setHeader(header);
            sessionValueProtocol.setData(session.getSessionId());

            protocol.addChild(sessionValueProtocol);
        }
    }

    private Protocol<?> createErrorProtocol() {
        Protocol<String> protocol = new Protocol<>();
        Header header = new Header();
        header.setType(Type.ERROR);
        header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
        header.setDataType(DataType.STRING);
        protocol.setHeader(header);
        protocol.setData(Code.ErrorCode.INTERNAL_SERVER_ERROR.toString());
        return protocol;
    }

    private void closeClient(AsynchronousSocketChannel client) {
        try {
            System.out.println("Closing client");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server.getINSTANCE().start();
    }
}
