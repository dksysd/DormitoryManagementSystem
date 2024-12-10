package client;

import client.core.util.AsyncRequest;
import shared.protocol.persistence.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

public class ClientExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String host = "localhost";
        int port = 4_000;

        for (int i = 0; i < 1; i++) {
            test(host, port);
        }

        Thread.currentThread().join();
    }

    private static void test(String host, int port) throws ExecutionException, InterruptedException {
        /*            AsyncClientConnector.connect(host, port).thenCompose(client -> {
                Protocol<?> requestProtocol = createProtocol();
                ByteBuffer requestBuffer = ProtocolSerializer.serialize(requestProtocol);
                requestBuffer.flip();
                CompletableFuture<AsynchronousSocketChannel> sendFuture = new CompletableFuture<>();
                client.write(requestBuffer, requestBuffer, new OutputHandler(client, Client::closeClient, sendFuture));
                return sendFuture;
            }).thenCompose(client -> {
                CompletableFuture<Protocol<?>> receiveFuture = new CompletableFuture<>();

                ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES);
                client.read(headerBuffer, headerBuffer, new InputHeaderHandler(client, Client::closeClient, receiveFuture));

                return receiveFuture;
            }).thenCompose(protocol -> {
                System.out.println(protocol);
                return null;
            });*/
        AsyncRequest asyncRequest = new AsyncRequest(host, port);
        for (int i = 0; i < 1; i++) {
            Protocol<?> requestProtocol = createProtocol();
            Protocol<?> resultProtocol = asyncRequest.sendAndReceive(requestProtocol);
            System.out.println(resultProtocol);
        }
    }

    public static CompletableFuture<AsynchronousSocketChannel> connectAsync(String host, int port) {
        CompletableFuture<AsynchronousSocketChannel> future = new CompletableFuture<>();

        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            client.connect(new InetSocketAddress(host, port), client, new CompletionHandler<Void, AsynchronousSocketChannel>() {
                @Override
                public void completed(Void result, AsynchronousSocketChannel attachment) {
                    future.complete(attachment);
                    System.out.println("Connected to " + host + ":" + port);
                }

                @Override
                public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                    future.completeExceptionally(exc);
                    try {
                        attachment.close();
                    } catch (IOException e) {
                        // 로깅 또는 추가 오류 처리
                    }
                }
            });
        } catch (IOException e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    private static Protocol<?> createProtocol() {
        Protocol<Protocol<?>> protocol = new Protocol<>();
        Header header = new Header();
        header.setType(Type.REQUEST);
        header.setCode(Code.RequestCode.LOGIN);
        header.setDataType(DataType.TLV);
        protocol.setHeader(header);

        Protocol<String> idProtocol = new Protocol<>();
        Header idHeader = new Header();
        idHeader.setType(Type.VALUE);
        idHeader.setCode(Code.ValueCode.ID);
        idHeader.setDataType(DataType.STRING);
        idProtocol.setHeader(idHeader);
        idProtocol.setData("Hello World");
        protocol.addChild(idProtocol);

        return protocol;
    }

    private static void closeClient(AsynchronousSocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getResponse(Protocol<?> protocol) {
        System.out.println(protocol.toString());
    }
}

