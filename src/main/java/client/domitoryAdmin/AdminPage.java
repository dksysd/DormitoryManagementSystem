package client.domitoryAdmin;

import client.core.util.AsyncRequest;

import java.util.Scanner;

/**
 * `AdminPage` 클래스는 관리자 화면의 기능들을 처리합니다.
 * <p>
 * 로그인한 관리자의 세션 정보를 유지하며, 관리자가 수행할 수 있는 다양한 관리 작업을 제공합니다.
 * 이 클래스에서는 선발 일정 등록, 입사 신청자 조회, 합격자 선발, 상벌점 관리, 퇴사 승인 등의 기능을 처리합니다.
 */
public class AdminPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;

    /**
     * 기본 생성자.
     * <p>
     * 세션 아이디 없이 객체를 생성합니다.
     */
    public AdminPage() {
    }

    /**
     * 세션 아이디를 초기화하는 생성자.
     *
     * @param sessionID 로그인한 관리자의 세션 ID
     */
    public AdminPage(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * 관리자 페이지에서 제공하는 기능을 출력하고 사용자의 선택을 받아 적절한 작업을 처리합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void adminFunction(AsyncRequest asyncRequest) {
        int option = 0;

        do {
            adminFunctionInfo();
            System.out.print("실행하려는 기능을 선택하세요 : ");
            option = sc.nextInt();
            System.out.println("=======================================");

            switch (option) {
                case 1:
                    registerSelectionInfo(asyncRequest);
                    break; // 선발 일정 등록
                case 2:
                    displayApplicants(asyncRequest);
                    break; // 입사 신청자 조회
                case 3:
                    selectApplicant(asyncRequest);
                    break; // 합격자 선발
                case 4:
                    managementMeritPoint(asyncRequest);
                    break; // 상벌점 관리
                case 5:
                    confirmTuberReport(asyncRequest);
                    break; // 결핵 진단서 승인
                case 6:
                    displayMoveOutApplicant(asyncRequest);
                    break; // 퇴사 승인
                case 7:
                    System.out.println("종료합니다.");
                    break; // 종료
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 시도하세요.");
                    break;
            }
        } while (option != 7);
    }

    /**
     * 관리자 페이지에서 수행할 수 있는 기능 정보를 출력합니다.
     */
    public static void adminFunctionInfo() {
        System.out.println("============= 관리자 페이지입니다 =============");
        System.out.println("1. 선발 일정 등록");
        System.out.println("2. 입사 신청자 목록 확인");
        System.out.println("3. 입사자 선발하기");
        System.out.println("4. 상벌점 관리");
        System.out.println("5. 결핵진단서 진위 확인");
        System.out.println("6. 퇴사 승인");
        System.out.println("7. 로그아웃");
        System.out.println();
    }

    /**
     * 특정 선발 일정 등록 기능을 처리합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void registerSelectionInfo(AsyncRequest asyncRequest) {
        // 관리자 선발 일정 등록 기능
        System.out.println("어떤 선발의 일정을 등록하시겠습니까?");
        // 세부적인 작업 처리 및 입력 요청
    }

    /**
     * 현재 입사 신청자의 목록을 조회하여 출력합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void displayApplicants(AsyncRequest asyncRequest) {
        System.out.println("=================== 입사 신청자 목록 ====================");
        // 신청자 목록 요청 및 출력 처리
    }

    /**
     * 현재 입사 신청자 중 합격자를 선발합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void selectApplicant(AsyncRequest asyncRequest) {
        System.out.println("선발/배정을 시작합니다.");
        // 합격자 선발 로직 처리
    }

    /**
     * 특정 학생의 상벌점을 추가하거나 수정합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void managementMeritPoint(AsyncRequest asyncRequest) {
        System.out.println("관리하려는 학생 상벌점 정보를 입력하세요.");
        // 상벌점 관리 로직 처리
    }

    /**
     * 결핵진단서를 확인하고 승인/거절 처리를 수행합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void confirmTuberReport(AsyncRequest asyncRequest) {
        System.out.println("승인 또는 거절을 선택하세요.");
        // 결핵진단서 확인 및 승인/거절 처리 로직
    }

    /**
     * 퇴사 신청자를 조회하고 승인 작업을 수행합니다.
     *
     * @param asyncRequest 서버와 비동기 통신을 위한 {@link AsyncRequest} 객체
     */
    public void displayMoveOutApplicant(AsyncRequest asyncRequest) {
        System.out.println("=================== 퇴사 신청자 목록 ====================");
        // 퇴사 신청자 목록 조회 및 승인 작업 처리
    }
}