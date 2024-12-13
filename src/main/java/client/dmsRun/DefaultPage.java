package client.dmsRun;

import client.auth.Auth;
import client.core.util.AsyncRequest;
import client.domitoryAdmin.AdminPage;
import client.domitoryUser.ApplicantPage;

import java.util.concurrent.ExecutionException;

/**
 * `DefaultPage` 클래스는 애플리케이션의 기본 진입점 역할을 하며 로그인 및 이후의 화면 선택 과정을 처리합니다.
 * <p>
 * 사용자가 관리자 또는 학생으로 로그인한 후, 적절한 화면으로 이동하거나 로그인이 실패하면 재시도를 유도합니다.
 */
public class DefaultPage {

    /**
     * 애플리케이션 실행 메서드.
     * <p>
     * 서버와의 비동기 연결을 설정하고, 사용자 로그인 상태에 따라 적절한 페이지를 로드합니다.
     *
     * @param host 서버의 호스트 이름 또는 IP 주소
     * @param port 서버의 포트 번호
     * @throws ExecutionException   서버 연결 또는 요청 처리 중 오류가 발생한 경우
     * @throws InterruptedException 작업이 중단된 경우
     */
    public static void run(String host, int port) throws ExecutionException, InterruptedException {
        AsyncRequest asyncRequest;
        try {
            asyncRequest = new AsyncRequest(host, port); // 서버와의 비동기 연결 초기화
        } catch (Exception e) {
            throw new RuntimeException(e); // 연결 실패 시 예외 발생
        }

        // 로그인 화면 시나리오 시작
        // 관리자 = 0, 학생 = 1, 로그인 실패 = -1
        Auth auth = new Auth();
        int identity = auth.logIn(asyncRequest); // 로그인 결과에 따라 화면 결정

        // 로그인 후 선택 화면 처리
        if (identity == 0) {
            // 관리자 화면 진입
            new AdminPage(auth.getSessionID()).adminFunction(asyncRequest);
        } else if (identity == 1) {
            // 학생 화면 진입
            new ApplicantPage(auth.getSessionID()).applicantFunction(asyncRequest);
        } else {
            // 로그인 실패 처리
            System.out.println("===============로그인 실패!===============\n\n");
            run(host, port); // 로그인 실패 시 재시도
        }

        auth.logOut(asyncRequest); // 로그아웃 처리
    }
}