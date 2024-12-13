package shared.protocol.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.exception.IllegalBufferSizeException;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.*;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * InputDataHandler 클래스는 비동기 데이터 처리를 위해 {@link CompletionHandler} 인터페이스를 구현한 클래스입니다.
 * 클라이언트로부터 데이터를 읽고, 완료되었을 때와 실패했을 때의 동작을 처리합니다.
 *
 * <p>클래스의 주요 역할:
 * <ul>
 *     <li>데이터 헤더 정보와 데이터를 처리하는 흐름을 관리합니다.</li>
 *     <li>채널에서 데이터를 성공적으로 읽었는지 검증하고, 오류가 있는 경우 적절한 처리를 수행합니다.</li>
 * </ul>
 *
 * <p>필드 설명:
 * <ul>
 *     <li>{@code header}: 읽어야 하는 데이터의 헤더 정보를 포함하는 {@link Header} 객체.</li>
 *     <li>{@code headerBuffer}: 데이터를 읽고 처리할 때 사용하는 {@link ByteBuffer} 버퍼.</li>
 *     <li>{@code inputHeaderHandler}: 데이터 처리 및 클라이언트 관리 용도로 사용되는 {@link InputHeaderHandler} 객체.</li>
 * </ul>
 */
@AllArgsConstructor
@Getter
public class InputDataHandler implements CompletionHandler<Integer, ByteBuffer> {
    protected final Header header;
    protected final ByteBuffer headerBuffer;
    protected final InputHeaderHandler inputHeaderHandler;

    /**
     * 데이터 읽기 작업이 성공적으로 완료되었을 때 호출됩니다.
     *
     * <p>동작 설명:
     * <ul>
     *     <li>데이터를 성공적으로 읽었는지 확인합니다.</li>
     *     <li>읽은 데이터 크기가 헤더에서 기대했던 데이터 길이와 일치하지 않는 경우 {@link IllegalBufferSizeException}을 발생시킵니다.</li>
     *     <li>데이터가 정상적으로 읽힌 경우 클라이언트의 원격 주소를 로깅합니다.</li>
     *     <li>결과가 -1인 경우 클라이언트의 연결을 닫는 동작을 수행합니다.</li>
     * </ul>
     *
     * @param result 읽은 바이트 수. -1이면 클라이언트 연결 종료 상태를 나타냄.
     * @param buffer 데이터를 저장한 {@link ByteBuffer}.
     * @throws IllegalBufferSizeException 읽은 데이터 크기가 예상된 크기와 일치하지 않을 경우 발생.
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            inputHeaderHandler.getCloseClientConsumer().accept(inputHeaderHandler.getClient());
            return;
        }

        if (buffer.position() != header.getDataLength()) {
            throw new IllegalBufferSizeException("Invalid data size (expected : " + header.getDataLength() + ", value : " + result + ")");
        }

        System.out.println("Read data from " + RemoteAddressResolver.getRemoteAddress(inputHeaderHandler.getClient()));

//        buffer.flip();
//        Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);
//        inputHeaderHandler.getCompleteConsumer().accept(inputHeaderHandler.getClient(), protocol);

//        headerBuffer.clear();
//        inputHeaderHandler.getClient().read(headerBuffer, headerBuffer, inputHeaderHandler);
    }

    /**
     * 데이터 읽기 작업이 실패했을 경우 호출됩니다.
     *
     * <p>동작 설명:
     * <ul>
     *     <li>발생한 예외의 스택 트레이스를 출력하여 디버깅 정보를 제공합니다.</li>
     * </ul>
     *
     * @param exc    읽기 작업 중 발생한 {@link Throwable} 예외.
     * @param buffer 작업을 진행 중이던 {@link ByteBuffer}.
     */
    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
