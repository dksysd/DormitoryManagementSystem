package client.core.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * `OutputHandler` 클래스는 비동기 소켓 통신에서 데이터를 전송하는 작업을 처리합니다.
 * <p>
 * 전송 작업의 성공 여부를 관리하며 결과를 비동기적으로 처리할 수 있습니다.
 * 해당 클래스는 `shared.protocol.handler.OutputHandler`를 상속하여 추가적인 기능을 제공합니다.
 */
public class OutputHandler extends shared.protocol.handler.OutputHandler {

    /**
     * 데이터 전송 작업 결과를 처리하기 위한 {@link CompletableFuture}.
     */
    private final CompletableFuture<AsynchronousSocketChannel> sendFuture;

    /**
     * `OutputHandler` 생성자.
     * <p>
     * 비동기 소켓 채널, 클라이언트 종료 처리를 위한 `Consumer`, 전송 완료를 알리기 위한 {@link CompletableFuture} 객체를 초기화합니다.
     *
     * @param client              클라이언트 비동기 소켓 채널
     * @param closeClientConsumer 클라이언트를 종료 처리하는 {@link Consumer}
     * @param sendFuture          데이터 전송 작업 완료를 알리기 위한 {@link CompletableFuture}
     */
    public OutputHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, CompletableFuture<AsynchronousSocketChannel> sendFuture) {
        super(client, closeClientConsumer);
        this.sendFuture = sendFuture;
    }

    /**
     * 비동기 데이터 전송 작업 완료 시 호출되는 메서드.
     * <p>
     * 전송이 정상적으로 완료되었으면 결과를 완료 상태로 설정하고, 디버깅 정보를 출력합니다.
     *
     * @param result 전송된 데이터 크기 (바이트 수)
     * @param buffer 전송된 데이터가 저장된 {@link ByteBuffer}
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) { // 전송 작업 성공
            System.out.println("Send data "); // 디버깅 메시지 출력
            sendFuture.complete(client); // 비동기 결과 완료 처리
        }
    }

    /**
     * 비동기 데이터 전송 작업 실패 시 호출되는 메서드.
     * <p>
     * 예외를 기록하고 비동기 작업 결과를 실패 상태로 설정합니다.
     *
     * @param exc    발생한 예외
     * @param buffer 작업이 수행된 {@link ByteBuffer}
     */
    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        super.failed(exc, buffer);
        sendFuture.completeExceptionally(exc); // 작업 실패 상태로 설정
    }
}