package client.core.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

public class AsyncClientConnector {
    public static CompletableFuture<AsynchronousSocketChannel> connect(String host, int port) {
        CompletableFuture<AsynchronousSocketChannel> connectionFuture = new CompletableFuture<>();

        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();

            client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Void>() {
                @Override
                public void completed(Void result, Void attachment) {
                    System.out.println("Connected to " + host + ":" + port);
                    connectionFuture.complete(client);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        // 로그 또는 추가 처리
                    }
                    connectionFuture.completeExceptionally(exc);
                }
            });

        } catch (IOException e) {
            connectionFuture.completeExceptionally(e);
        }

        return connectionFuture;
    }

    // 타임아웃 옵션 포함 오버로드 메서드
    public static CompletableFuture<AsynchronousSocketChannel> connect(String host, int port, long timeoutSeconds) {
        CompletableFuture<AsynchronousSocketChannel> connectionFuture = connect(host, port);

        // 타임아웃 설정
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            if (!connectionFuture.isDone()) {
                connectionFuture.completeExceptionally(new TimeoutException("Connection timeout"));
            }
            scheduler.shutdown();
        }, timeoutSeconds, TimeUnit.SECONDS);

        return connectionFuture;
    }
}