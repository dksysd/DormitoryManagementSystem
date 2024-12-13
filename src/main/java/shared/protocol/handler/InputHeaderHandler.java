package shared.protocol.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.exception.IllegalBufferSizeException;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.*;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

/**
 * InputHeaderHandler 클래스는 비동기 소켓 통신 환경에서 헤더 데이터를 처리하기 위해 설계된 클래스입니다.
 * {@link CompletionHandler} 인터페이스를 구현하여 데이터 읽기의 성공 및 실패에 대한 동작을 정의합니다.
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>클라이언트에서 보낸 헤더 데이터를 읽고 유효성을 검증합니다.</li>
 *     <li>읽은 데이터 크기가 예상된 헤더 크기와 일치하지 않는 경우 예외를 발생시킵니다.</li>
 *     <li>읽은 결과가 -1(EOF)인 경우 클라이언트의 연결을 종료합니다.</li>
 *     <li>완료 후 데이터를 후속 핸들러로 전달하거나, 오류 시 디버깅 정보를 제공합니다.</li>
 * </ul>
 * <p>
 * 필드 설명:
 * <ul>
 *     <li>{@code client}: 데이터를 송수신하는 {@link AsynchronousSocketChannel} 클라이언트 채널.</li>
 *     <li>{@code closeClientConsumer}: 클라이언트 연결을 닫는 작업을 수행하는 {@link Consumer}.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class InputHeaderHandler implements CompletionHandler<Integer, ByteBuffer> {
    protected final AsynchronousSocketChannel client;
    protected final Consumer<AsynchronousSocketChannel> closeClientConsumer;
//    private final BiConsumer<AsynchronousSocketChannel, Protocol<?>> completeConsumer;

    /**
     * 비동기 데이터 읽기 작업이 성공적으로 완료되었을 때 호출되는 메서드입니다.
     * <p>
     * 작업이 성공한 경우 읽은 데이터 크기를 검증하여 헤더 데이터가 유효한지 확인하고,
     * 작업 후속 처리를 위해 완전한 헤더 데이터를 처리합니다.
     * 결과가 -1인 경우, 클라이언트의 연결을 닫는 동작을 수행합니다.
     *
     * @param result 읽은 데이터의 바이트 수. -1이면 클라이언트 연결 종료 상태를 나타냄.
     * @param buffer 읽은 데이터가 저장된 {@link ByteBuffer}.
     * @throws IllegalBufferSizeException 읽은 데이터 크기가 예상된 헤더 크기와 일치하지 않을 경우 발생.
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            closeClientConsumer.accept(client);
            return;
        }

        if (result != Header.BYTES) {
            throw new IllegalBufferSizeException("Invalid header size (expected : " + Header.BYTES + ", value : " + result + ")");
        }

        System.out.println("Read header from " + RemoteAddressResolver.getRemoteAddress(client));

        /*buffer.flip();
        Header header = ProtocolParser.parseHeader(buffer);
        ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength());
        client.read(dataBuffer, dataBuffer, new InputDataHandler(header, buffer, this));*/
    }

    /**
     * 비동기 데이터 읽기 작업이 실패했을 때 호출되는 메서드입니다.
     * <p>
     * 발생한 예외의 스택 트레이스를 출력하여 디버깅 정보를 제공합니다.
     *
     * @param exc    데이터 작업 중 발생한 예외 객체.
     * @param buffer 실패 시 작업 중이던 데이터가 저장된 {@link ByteBuffer}.
     */
    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
