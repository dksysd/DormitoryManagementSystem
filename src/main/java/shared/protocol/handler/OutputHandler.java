package shared.protocol.handler;

import lombok.AllArgsConstructor;
import server.util.RemoteAddressResolver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

/**
 * OutputHandler 클래스는 비동기 소켓 통신 환경에서 데이터를 쓰기 위한 핸들러로,
 * {@link CompletionHandler} 인터페이스를 구현하여 데이터 쓰기 작업의 결과(성공 또는 실패)에 따라 동작을 정의합니다.
 * <p>
 * 주요 역할:
 * <ul>
 *     <li>클라이언트에 데이터를 비동기로 전송하고, 전송이 지속적으로 이루어질 수 있도록 처리합니다.</li>
 *     <li>데이터 쓰기 작업이 실패하거나 완료된 경우 적절한 후속 작업(예: 클라이언트 연결 종료)을 수행합니다.</li>
 * </ul>
 * <p>
 * 필드 설명:
 * <ul>
 *     <li>{@code client}: 데이터를 보내기 위한 {@link AsynchronousSocketChannel} 클라이언트 채널입니다.</li>
 *     <li>{@code closeClientConsumer}: 클라이언트 연결을 닫는 동작을 수행하는 {@link Consumer} 객체입니다.</li>
 * </ul>
 */
@AllArgsConstructor
public class OutputHandler implements CompletionHandler<Integer, ByteBuffer> {
    protected final AsynchronousSocketChannel client;
    protected final Consumer<AsynchronousSocketChannel> closeClientConsumer;

    /**
     * 비동기 데이터 쓰기 작업이 성공적으로 완료되었을 때 호출되는 메서드입니다.
     * <p>
     * 작업이 성공한 경우 데이터를 남은 부분이 있는지 확인한 뒤,
     * 남아 있는 데이터를 계속 쓰기 작업으로 처리합니다.
     * 결과가 -1인 경우, 클라이언트의 연결을 종료합니다.
     *
     * @param result 쓰기 작업으로 전송된 바이트 수. -1이면 클라이언트 연결이 끊긴 상태를 나타냅니다.
     * @param buffer 전송할 데이터가 저장된 {@link ByteBuffer}.
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            closeClientConsumer.accept(client);
        }

        if (buffer.hasRemaining()) {
            System.out.println("Send data to " + RemoteAddressResolver.getRemoteAddress(client));
            client.write(buffer, buffer, this);
        }
    }

    /**
     * 비동기 데이터 쓰기 작업 중 실패했을 때 호출되는 메서드입니다.
     * <p>
     * 작업 중 발생한 예외의 스택 트레이스를 출력하여 디버깅 정보를 제공합니다.
     *
     * @param exc    쓰기 작업 중 발생한 예외 객체.
     * @param buffer 실패 시 작업 중이던 데이터를 포함한 {@link ByteBuffer}.
     */
    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
