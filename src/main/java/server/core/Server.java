package server.core;

import lombok.Getter;
import server.config.Config;
import server.config.DatabaseConnectionPool;
import server.config.RequestHandlerInitializer;
import server.core.handler.AcceptHandler;
import server.core.handler.OutputHandler;
import server.core.handler.RequestHandler;
import server.core.persistence.Session;
import server.core.persistence.WorkItem;
import shared.protocol.persistence.*;
import shared.protocol.util.ProtocolSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

/**
 * Server 클래스는 애플리케이션의 메인 서버 역할을 수행합니다.
 * 요청 수신, 작업 처리, 응답 전송을 포함하여 서버의 전체 동작을 관리하며,
 * 비동기 처리 및 작업 큐를 사용하여 다중 클라이언트 요청을 효율적으로 처리합니다.
 */
public class Server {

    /**
     * Server 싱글톤 인스턴스
     */
    @Getter
    public static final Server INSTANCE = new Server();

    /**
     * 서버가 수신 대기할 포트 번호
     */
    private final int port;

    /**
     * 작업을 처리할 워커 스레드의 개수
     */
    private final int workerThreads;

    /**
     * 워커 스레드 풀을 관리하는 실행자 서비스
     */
    private final ExecutorService workerExecutor;

    /**
     * 처리할 작업 항목(WorkItem)을 관리하는 작업 큐
     */
    private final BlockingQueue<WorkItem> workQueue;

    /**
     * 요청을 처리하는 {@link RequestHandler} 인스턴스
     */
    private final RequestHandler requestHandler;

    /**
     * Server 생성자.
     * 서버의 기본 설정을 초기화하며, 연결 풀 및 워커 스레드를 준비합니다.
     * 서버는 싱글톤 패턴으로 설계되어 직접적으로 인스턴스를 생성할 수 없습니다.
     */
    private Server() {
        port = Config.getInt("PORT");
        workerThreads = Config.getInt("WORKER_THREADS");
        int workQueueCapacity = Config.getInt("QUEUE_CAPACITY");

        this.workerExecutor = Executors.newFixedThreadPool(workerThreads);
        this.workQueue = new LinkedBlockingQueue<>(workQueueCapacity);
        this.requestHandler = RequestHandler.getINSTANCE();

        for (int i = 0; i < workerThreads; i++) {
            workerExecutor.submit(() -> {
            }); // 빈 작업 제출 (초기화 작업)
        }

        DatabaseConnectionPool.load();
    }

    /**
     * 서버를 시작합니다.
     * 워커 스레드를 실행하고, 요청 수신 핸들러를 설정한 후
     * 클라이언트 요청을 수신 대기합니다.
     */
    public void start() {
        startWorker();

        RequestHandlerInitializer.init(this.requestHandler);

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port), 10_000)) {
            server.accept(server, new AcceptHandler(this::closeClient, workQueue));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 워커 스레드를 시작합니다.
     * 각 스레드는 작업 큐에서 WorkItem을 가져와 처리를 수행합니다.
     */
    private void startWorker() {
        for (int i = 0; i < workerThreads; i++) {
            workerExecutor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        WorkItem workItem = workQueue.take();
                        work(workItem);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    /**
     * 특정 WorkItem에 대한 작업을 처리합니다.
     * 클라이언트 요청을 처리하고, 응답 프로토콜을 생성하여 클라이언트로 전송합니다.
     *
     * @param workItem 처리할 작업 항목
     */
    private void work(WorkItem workItem) {
        checkSessionId(workItem.getRequestProtocol());
        System.out.println("Working " + workItem);
        Protocol<?> protocol;
        try {
            protocol = requestHandler.request(workItem.getRequestProtocol());
        } catch (Exception e) {
            protocol = createErrorProtocol();
            e.printStackTrace();
        }
        workItem.setResponseProtocol(protocol);
        ByteBuffer buffer = ProtocolSerializer.serialize(protocol);
        buffer.flip();
        workItem.getClient().write(buffer, buffer, new OutputHandler(workItem, this::closeClient));
    }

    /**
     * 요청 프로토콜 내에서 세션 ID를 확인하고, 필요한 경우 새로운 세션을 생성하여 추가합니다.
     *
     * @param protocol 클라이언트 요청 프로토콜
     */
    private void checkSessionId(Protocol<?> protocol) {
        boolean hasSessionId = protocol.getChildren().stream().anyMatch(child -> child.getHeader().getCode() == Code.ValueCode.SESSION_ID);
        if (!hasSessionId) {
            Session session = SessionManager.getINSTANCE().createSession();

            Protocol<String> sessionValueProtocol = new Protocol<>();
            Header header = new Header();
            header.setType(Type.VALUE);
            header.setCode(Code.ValueCode.SESSION_ID);
            header.setDataType(DataType.STRING);
            sessionValueProtocol.setHeader(header);
            sessionValueProtocol.setData(session.getSessionId());

            protocol.addChild(sessionValueProtocol);
        }
    }

    /**
     * 내부 서버 에러를 나타내는 기본 에러 응답 프로토콜을 생성합니다.
     *
     * @return 에러 응답 프로토콜
     */
    private Protocol<?> createErrorProtocol() {
        Protocol<String> protocol = new Protocol<>();
        Header header = new Header();
        header.setType(Type.ERROR);
        header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
        header.setDataType(DataType.STRING);
        protocol.setHeader(header);
        protocol.setData(Code.ErrorCode.INTERNAL_SERVER_ERROR.toString());
        return protocol;
    }

    /**
     * 클라이언트와의 연결을 닫습니다.
     *
     * @param client 닫을 클라이언트의 {@link AsynchronousSocketChannel}
     */
    private void closeClient(AsynchronousSocketChannel client) {
        try {
            System.out.println("Closing client");
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 서버 애플리케이션의 진입점입니다.
     * {@link Server} 인스턴스를 가져와 서버를 시작합니다.
     *
     * @param args 명령줄 인수
     */
    public static void main(String[] args) {
        Server.getINSTANCE().start();
    }
}