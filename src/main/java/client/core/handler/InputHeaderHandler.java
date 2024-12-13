package client.core.handler;

import lombok.Getter;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * `InputHeaderHandler` 클래스는 비동기 소켓 통신에서 수신되는 헤더 데이터를 처리하는 역할을 담당합니다.
 * <p>
 * 수신된 헤더 데이터를 파싱하고, 이후 데이터 본문 처리를 위한 `InputDataHandler`를 호출합니다.
 * 해당 클래스는 `shared.protocol.handler.InputHeaderHandler`를 상속하여 확장된 기능을 제공합니다.
 */
@Getter
public class InputHeaderHandler extends shared.protocol.handler.InputHeaderHandler {

    /**
     * 요청 처리 결과를 비동기적으로 반환하기 위한 {@link CompletableFuture} 객체.
     */
    protected final CompletableFuture<Protocol<?>> resultFuture;

    /**
     * `InputHeaderHandler` 생성자.
     * <p>
     * 비동기 소켓 채널, 클라이언트 종료 처리를 위한 `Consumer`, 비동기 결과 객체를 초기화합니다.
     *
     * @param client              클라이언트 비동기 소켓 채널
     * @param closeClientConsumer 클라이언트를 종료 처리하는 {@link Consumer}
     * @param resultFuture        비동기 결과를 반환하기 위한 {@link CompletableFuture}
     */
    public InputHeaderHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, CompletableFuture<Protocol<?>> resultFuture) {
        super(client, closeClientConsumer);
        this.resultFuture = resultFuture;
    }

    /**
     * 비동기 작업 완료 시 호출되는 메서드.
     * <p>
     * 수신된 데이터가 올바르다면 헤더를 파싱하고, 데이터 본문 처리를 위해 `InputDataHandler`를 호출합니다.
     *
     * @param result 읽은 데이터의 양 (바이트 수)
     * @param buffer 데이터가 저장된 {@link ByteBuffer}
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) { // 올바른 결과가 있다면 헤더 파싱 진행
            buffer.flip(); // 버퍼를 읽기 모드로 변경
            Header header = ProtocolParser.parseHeader(buffer); // 헤더 데이터 파싱
            System.out.println(header); // 디버깅을 위한 헤더 출력
            ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength()); // 데이터 버퍼 생성
            client.read(dataBuffer, dataBuffer, new InputDataHandler(header, buffer, this, resultFuture)); // 데이터 처리 핸들러 실행
        }
    }
}