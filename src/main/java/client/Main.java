package client;

import client.dmsRun.DefaultPage;

import java.util.concurrent.ExecutionException;

/**
 * `Main` 클래스는 애플리케이션의 진입점(Entry Point)입니다.
 * <p>
 * 프로그램 실행 시 맨 처음 호출되며, 기본 페이지를 실행하고 네트워크 연결을 설정하여 전체 애플리케이션의 작동을 시작합니다.
 */
public class Main {

    /**
     * 애플리케이션의 실행 메서드입니다.
     *
     * @param args 프로그램 실행 시 전달받는 명령어 인수
     * @throws ExecutionException   비동기 작업 실행 중 예외가 발생할 경우
     * @throws InterruptedException 현재 스레드가 작업 실행 중 인터럽트될 경우
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String host = "172.30.126.66"; // 서버 호스트 주소
        int port = 4_000; // 서버 포트 번호

        // 기본 페이지 실행
        DefaultPage.run(host, port);

        // 현재 스레드를 유지하여 애플리케이션이 종료되지 않도록 설정
        Thread.currentThread().join();
    }
}