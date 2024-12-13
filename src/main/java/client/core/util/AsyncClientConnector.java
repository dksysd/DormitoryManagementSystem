package client.core.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

/**
 * `AsyncClientConnector` 클래스는 비동기 방식으로 서버에 연결을 설정하는 기능을 제공합니다.
 * <p>
 * 비동기 소켓 채널을 이용하여 서버와 연결하고, 이를 비동기 작업 결과인 {@link CompletableFuture}로 반환합니다.
 */
public class AsyncClientConnector {

    /**
     * 주어진 호스트와 포트 번호에 비동기적으로 연결을 시도합니다.
     *
     * @param host 연결할 서버의 호스트 이름 또는 IP 주소
     * @param port 연결할 서버의 포트 번호
     * @return 서버와의 연결이 완료된 {@link AsynchronousSocketChannel}을 포함하는 {@link CompletableFuture}
     */
    public static CompletableFuture<AsynchronousSocketChannel> connect(String host, int port) {
        CompletableFuture<AsynchronousSocketChannel> connectionFuture = new CompletableFuture<>();

        try {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();

            client.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Void>() {
                /**
                 * 비동기 연결이 성공적으로 완료되었을 때 호출되는 메서드.
                 *
                 * @param result    작업 결과 (사용되지 않음)
                 * @param attachment 연결 시 첨부된 데이터 (사용되지 않음)
                 */
                @Override
                public void completed(Void result, Void attachment) {
                    System.out.println("Connected to " + host + ":" + port);
                    connectionFuture.complete(client); // 연결 성공 시 결과 완료
                }

                /**
                 * 비동기 연결이 실패했을 때 호출되는 메서드.
                 *
                 * @param exc        발생한 예외
                 * @param attachment 연결 시 첨부된 데이터 (사용되지 않음)
                 */
                @Override
                public void failed(Throwable exc, Void attachment) {
                    try {
                        client.close(); // 연결 실패 시 클라이언트 닫기
                    } catch (IOException e) {
                        // 실패 로그 처리 또는 추가 작업
                    }
                    connectionFuture.completeExceptionally(exc); // 실패 상태로 완료 설정
                }
            });

        } catch (IOException e) {
            connectionFuture.completeExceptionally(e); // 클라이언트 설정 중 예외 발생 처리
        }

        return connectionFuture;
    }

    /**
     * 주어진 타임아웃 옵션과 함께 비동기 연결을 시도합니다.
     *
     * @param host           연결할 서버의 호스트 이름 또는 IP 주소
     * @param port           연결할 서버의 포트 번호
     * @param timeoutSeconds 연결 타임아웃 제한 (초 단위)
     * @return 서버와의 연결이 완료된 {@link AsynchronousSocketChannel}을 포함하는 {@link CompletableFuture}
     */
    public static CompletableFuture<AsynchronousSocketChannel> connect(String host, int port, long timeoutSeconds) {
        CompletableFuture<AsynchronousSocketChannel> connectionFuture = connect(host, port);

        // 타임아웃 설정
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            if (!connectionFuture.isDone()) {
                connectionFuture.completeExceptionally(new TimeoutException("Connection timeout")); // 타임아웃 예외 처리
            }
            scheduler.shutdown(); // 스케줄러 종료
        }, timeoutSeconds, TimeUnit.SECONDS);

        return connectionFuture;
    }
}