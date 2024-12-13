package server.core.handler;

import lombok.AllArgsConstructor;
import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.Header;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * AcceptHandler 클래스는 {@link AsynchronousServerSocketChannel}에서 들어오는 클라이언트 연결을
 * 비동기로 처리하는 역할을 합니다. 클라이언트가 연결되었을 때 초기 작업을 처리하고,
 * 읽기 작업을 다음 처리기로 위임합니다.
 */
@AllArgsConstructor
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    /**
     * 클라이언트 연결 종료 시 실행할 {@link Consumer} 객체
     */
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;

    /**
     * 작업 처리 요청을 저장하는 {@link BlockingQueue}
     */
    private final BlockingQueue<WorkItem> workQueue;

    /**
     * 클라이언트 연결이 성공적으로 완료되었을 때 호출됩니다.
     * 클라이언트의 원격 주소를 확인하고, 초기 헤더 읽기 작업을 {@link InputHeaderHandler}로 위임합니다.
     * 또한, 서버는 다음 클라이언트 연결을 계속 수락할 수 있도록 준비합니다.
     *
     * @param client 새로 연결된 클라이언트 채널
     * @param server 현재 요청을 수락한 서버 채널
     */
    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
        // 다음 클라이언트 연결을 계속 수락
        server.accept(server, this);

        // 연결된 클라이언트의 원격 주소 출력
        System.out.println("Server accepted from " + RemoteAddressResolver.getRemoteAddress(client));

        // 헤더 버퍼를 준비하고 읽기 처리기를 등록
        ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES);
        client.read(headerBuffer, headerBuffer, new InputHeaderHandler(client, closeClientConsumer, workQueue));
    }

    /**
     * 클라이언트 연결이 실패했을 경우 호출됩니다.
     * 실패 원인을 출력합니다.
     *
     * @param exc    연결 실패의 원인이 되는 예외
     * @param server 요청을 수락하려던 서버 채널
     */
    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel server) {
        exc.printStackTrace();
    }
}