package server.core.handler;

import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;
import shared.protocol.handler.InputHeaderHandler;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

/**
 * InputDataHandler 클래스는 클라이언트로부터 수신된 데이터를 처리하는 작업을 담당합니다.
 * {@link shared.protocol.handler.InputDataHandler}를 확장하며, 데이터를 파싱하고 이를 처리할 작업 항목으로 변환하여
 * 작업 큐에 추가합니다.
 */
public class InputDataHandler extends shared.protocol.handler.InputDataHandler {

    /**
     * 클라이언트 요청을 처리하기 위해 작업 항목을 저장하는 큐
     */
    private final BlockingQueue<WorkItem> workQueue;

    /**
     * InputDataHandler 생성자.
     * 부모 클래스에서 필요한 정보를 전달받아 초기화하며, 작업 큐를 설정합니다.
     *
     * @param header             입력 데이터의 {@link Header} 정보
     * @param headerBuffer       헤더를 읽을 때 사용하는 {@link ByteBuffer}
     * @param inputHeaderHandler 상위 입력 헤더 핸들러 인스턴스
     * @param workQueue          처리할 작업 항목을 저장하는 큐
     */
    public InputDataHandler(Header header, ByteBuffer headerBuffer, InputHeaderHandler inputHeaderHandler, BlockingQueue<WorkItem> workQueue) {
        super(header, headerBuffer, inputHeaderHandler);
        this.workQueue = workQueue;
    }

    /**
     * 클라이언트로부터 데이터를 성공적으로 읽어왔을 때 호출됩니다.
     * 데이터를 파싱하여 {@link WorkItem} 객체로 변환한 뒤, 작업 큐에 추가합니다.
     * 작업이 완료된 뒤, 추가 데이터를 읽을 준비를 위해 버퍼와 클라이언트를 재설정합니다.
     *
     * @param result 읽어들인 바이트 수
     * @param buffer 읽어들인 데이터가 저장된 {@link ByteBuffer}
     */
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        // 부모 클래스 처리 호출
        super.completed(result, buffer);

        if (result != -1) {
            // 버퍼 데이터를 읽기 위해 flip
            buffer.flip();

            // 프로토콜 데이터 파싱
            Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);

            // WorkItem 생성하여 작업 큐에 추가
            WorkItem workItem = new WorkItem(inputHeaderHandler.getClient(), protocol, System.currentTimeMillis());
            workQueue.add(workItem);

            // 데이터 수신 완료 메시지 출력
            System.out.println("Data received from " + RemoteAddressResolver.getRemoteAddress(inputHeaderHandler.getClient())
                    + " : " + protocol);

            // 추가 데이터를 읽기 위해 버퍼 초기화 및 재설정
            headerBuffer.clear();
            inputHeaderHandler.getClient().read(headerBuffer, headerBuffer, inputHeaderHandler);
        }
    }
}
