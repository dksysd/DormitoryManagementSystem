package server.core.handler;

import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Consumer;

/**
 * OutputHandler 클래스는 클라이언트로 응답 데이터를 비동기로 전송하는 역할을 합니다.
 * {@link shared.protocol.handler.OutputHandler}를 확장하며, 응답 전송 작업 완료 후 추가 작업을 수행합니다.
 */
public class OutputHandler extends shared.protocol.handler.OutputHandler {

    /**
     * 클라이언트 요청을 처리하고 응답을 생성하기 위한 작업 항목
     */
    private final WorkItem workItem;

    /**
     * OutputHandler 생성자.
     * 작업 항목과 클라이언트 종료 처리기를 설정하여 응답 처리를 초기화합니다.
     *
     * @param workItem            현재 클라이언트 요청을 처리하는 {@link WorkItem}
     * @param closeClientConsumer 클라이언트 연결 종료 시 호출할 작업
     */
    public OutputHandler(WorkItem workItem, Consumer<AsynchronousSocketChannel> closeClientConsumer) {
        super(workItem.getClient(), closeClientConsumer);
        this.workItem = workItem;
    }

    /**
     * 클라이언트로의 응답 데이터 전송이 성공적으로 완료되었을 때 호출됩니다.
     * 응답 처리를 마무리하고 로그 출력을 수행합니다.
     *
     * @param result 전송된 바이트 수
     * @param buffer 전송된 데이터가 저장된 {@link ByteBuffer}
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        // 부모 클래스의 완료 처리 호출
        super.completed(result, buffer);

        // 응답 전송 완료 후 추가 작업 수행
        endResponse();
    }

    /**
     * 응답 처리가 완료되었음을 나타내는 메시지를 출력합니다.
     * 클라이언트 주소 및 응답 프로토콜 정보와 함께 작업 시간(ms)을 출력합니다.
     */
    private void endResponse() {
        System.out.printf(
                "Send (%s) %s -> %s [%dms]\n",
                RemoteAddressResolver.getRemoteAddress(workItem.getClient()),
                workItem.getResponseProtocol().toString(),
                workItem.getResponseProtocol().toString(),
                System.currentTimeMillis() - workItem.getTimestamp()
        );
    }
}