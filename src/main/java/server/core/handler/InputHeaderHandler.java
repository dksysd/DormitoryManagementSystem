package server.core.handler;

import server.core.persistence.WorkItem;
import shared.protocol.persistence.Header;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * InputHeaderHandler 클래스는 클라이언트로부터 수신된 데이터를 처리하기 전, 요청 데이터의 헤더를 처리하는 작업을 담당합니다.
 * {@link shared.protocol.handler.InputHeaderHandler}를 확장하며, 헤더 데이터를 읽고,
 * 이를 기반으로 데이터 처리기를 초기화하여 본문 데이터를 읽는 작업을 이어 나갑니다.
 */
public class InputHeaderHandler extends shared.protocol.handler.InputHeaderHandler {

    /**
     * 클라이언트 요청을 처리하기 위해 작업 항목을 저장하는 큐
     */
    private final BlockingQueue<WorkItem> workQueue;

    /**
     * InputHeaderHandler 생성자.
     * 부모 클래스에서 필요한 클라이언트 및 종료 처리기 정보를 설정하며, 작업 큐를 초기화합니다.
     *
     * @param client              처리 중인 클라이언트 {@link AsynchronousSocketChannel}
     * @param closeClientConsumer 클라이언트 종료 시 실행할 작업
     * @param workQueue           처리할 작업 항목을 저장하는 큐
     */
    public InputHeaderHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, BlockingQueue<WorkItem> workQueue) {
        super(client, closeClientConsumer);
        this.workQueue = workQueue;
    }

    /**
     * 클라이언트로부터 헤더 데이터를 성공적으로 읽어왔을 때 호출됩니다.
     * 데이터를 파싱하여 {@link Header} 객체로 변환한 뒤, 본문 데이터를 읽기 위한 새로운 데이터를 처리기로 위임합니다.
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

            // 헤더 데이터 파싱
            Header header = ProtocolParser.parseHeader(buffer);

            // 헤더에서 데이터를 읽을 크기를 지정한 버퍼 생성
            ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength());

            // 본문 데이터를 처리할 InputDataHandler 등록
            client.read(dataBuffer, dataBuffer, new InputDataHandler(header, buffer, this, workQueue));
        }
    }
}