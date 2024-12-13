package client.core.handler;

import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * `InputDataHandler` 클래스는 입력 데이터를 처리하는 역할을 담당합니다.
 * <p>
 * 수신한 데이터 버퍼를 기반으로 프로토콜 데이터를 분석하고 결과를 비동기적으로 처리합니다.
 * 해당 클래스는 `shared.protocol.handler.InputDataHandler`를 상속하여 확장된 기능을 제공합니다.
 */
public class InputDataHandler extends shared.protocol.handler.InputDataHandler {

    /**
     * 요청 처리 결과를 비동기적으로 반환하기 위한 {@link CompletableFuture} 객체.
     */
    protected final CompletableFuture<Protocol<?>> resultFuture;

    /**
     * `InputDataHandler`의 생성자.
     * <p>
     * 필요한 헤더 정보와 데이터 버퍼를 초기화하고, 입력 헤더 처리기를 설정합니다.
     *
     * @param header             입력 데이터의 {@link Header} 객체
     * @param headerBuffer       헤더 데이터를 담고 있는 {@link ByteBuffer}
     * @param inputHeaderHandler 헤더 처리기를 담당하는 {@link InputHeaderHandler}
     * @param resultFuture       비동기 결과 반환 객체
     */
    public InputDataHandler(Header header, ByteBuffer headerBuffer, InputHeaderHandler inputHeaderHandler, CompletableFuture<Protocol<?>> resultFuture) {
        super(header, headerBuffer, inputHeaderHandler);
        this.resultFuture = resultFuture;
    }

    /**
     * 비동기 작업 완료 시 호출되는 메서드.
     * <p>
     * 데이터를 처리한 후, 프로토콜 객체를 생성하여 {@code resultFuture}를 완료 상태로 설정합니다.
     *
     * @param result 읽은 데이터의 양 (바이트 수)
     * @param buffer 데이터 버퍼
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) { // 유효한 결과가 있으면 작업 수행
            buffer.flip(); // 버퍼를 읽기 모드로 전환
            Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer); // 프로토콜 파싱
            resultFuture.complete(protocol); // 완료 결과 전달
        }
    }

    /**
     * 비동기 작업 실패 시 호출되는 메서드.
     * <p>
     * 예외를 {@code resultFuture}에 설정하여 실패 상태로 처리합니다.
     *
     * @param exc    발생한 예외
     * @param buffer 작업에 사용된 데이터 버퍼
     */
    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        super.failed(exc, buffer);
        resultFuture.completeExceptionally(exc); // 실패 결과 전달
    }
}