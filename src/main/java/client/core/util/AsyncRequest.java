package client.core.util;

import client.core.handler.InputHeaderHandler;
import client.core.handler.OutputHandler;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolSerializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * `AsyncRequest` 클래스는 비동기 소켓 통신을 통해 서버와 요청/응답을 처리하는 유틸리티 클래스입니다.
 * <p>
 * 서버로 Protocol 요청을 전송하고, 그에 대한 응답을 비동기적으로 처리합니다.
 */
public class AsyncRequest {

    /**
     * 비동기 소켓 연결을 통해 요청을 보낼 클라이언트 채널.
     */
    private final AsynchronousSocketChannel client;

    /**
     * `AsyncRequest` 생성자.
     * <p>
     * 주어진 호스트와 포트를 사용하여 서버에 비동기 연결을 설정합니다.
     *
     * @param host 서버의 호스트 이름 또는 IP 주소
     * @param port 서버의 포트 번호
     * @throws ExecutionException   연결 중 오류가 발생했을 때 발생
     * @throws InterruptedException 작업이 중단되었을 때 발생
     */
    public AsyncRequest(String host, int port) throws ExecutionException, InterruptedException {
        client = AsyncClientConnector.connect(host, port).get(); // 서버 연결 시도
    }

    /**
     * Protocol 객체를 서버로 직렬화하여 전송합니다.
     *
     * @param requestProtocol 전송할 {@link Protocol} 객체
     * @return 전송 작업이 완료된 클라이언트 소켓을 포함하는 {@link CompletableFuture}
     */
    private CompletableFuture<AsynchronousSocketChannel> sendRequest(Protocol<?> requestProtocol) {
        ByteBuffer requestBuffer = ProtocolSerializer.serialize(requestProtocol); // Protocol 객체를 직렬화
        requestBuffer.flip();

        CompletableFuture<AsynchronousSocketChannel> sendFuture = new CompletableFuture<>();
        client.write(requestBuffer, requestBuffer, new OutputHandler(client, this::closeClient, sendFuture)); // 데이터 전송

        return sendFuture;
    }

    /**
     * 서버로부터 응답을 수신합니다.
     *
     * @return 수신된 {@link Protocol} 객체를 포함하는 {@link CompletableFuture}
     */
    private CompletableFuture<Protocol<?>> receiveResponse() {
        CompletableFuture<Protocol<?>> responseFuture = new CompletableFuture<>();
        ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES); // 헤더 크기만큼 읽기 위한 버퍼 준비
        client.read(headerBuffer, headerBuffer, new InputHeaderHandler(client, this::closeClient, responseFuture)); // 응답 처리
        return responseFuture;
    }

    /**
     * 서버로 요청을 전송하고 응답을 차례로 처리합니다.
     *
     * @param requestProtocol 전송할 {@link Protocol} 객체
     * @return 서버로부터 수신한 {@link Protocol} 객체
     * @throws ExecutionException   요청/응답 과정 중 오류가 발생했을 때 발생
     * @throws InterruptedException 작업이 중단되었을 때 발생
     */
    public Protocol<?> sendAndReceive(Protocol<?> requestProtocol) throws ExecutionException, InterruptedException {
        return sendRequest(requestProtocol) // 요청 전송
                .thenCompose(client -> receiveResponse()) // 요청 전송 성공 후 응답 수신
                .get(); // 동기적으로 결과 반환
    }

    /**
     * 비동기 소켓 채널을 닫아 자원을 해제합니다.
     *
     * @param client 닫을 {@link AsynchronousSocketChannel} 객체
     */
    private void closeClient(AsynchronousSocketChannel client) {
        try {
            client.close(); // 소켓 닫기
        } catch (IOException e) {
            e.printStackTrace(); // 오류 로그 출력
        }
    }
}