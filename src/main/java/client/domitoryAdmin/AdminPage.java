package client.domitoryAdmin;

import java.util.Scanner;

public class AdminPage {
    private static Scanner sc = new Scanner(System.in);

    //스위치문 있는 선택지별로 결과 돌리는 메서드 만들깅 >> 로그아웃 전까지 반복.
    public static void adminFunction(){
        int option = 0;

        do{
            adminFunctionInfo();
            System.out.print("실행하려는 기능을 선택하세요 : ");
            option = sc.nextInt();
            System.out.println("=======================================");

            switch (option){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                default: break;
            }
        } while (option != 8);
    }

    public static void adminFunctionInfo(){
        System.out.println("============= 관리자 페이지입니다 =============");
        System.out.println("1. register selection information 이거 머임?");
        System.out.println("2. 입사 신청자 목록 확인");
        System.out.println("3. 입사자 선발하기");
        System.out.println("4. 상벌점 관리");
        System.out.println("5. 결핵진단서 진위 확인");
        System.out.println("6. 퇴사 승인");
        System.out.println("7. 우선선발 증빙서 진위 확인");
        System.out.println("8. 로그아웃");
        System.out.println();
        System.out.println();
    }

    public static void registerSelectionInfo(){

    }

    public static void displayApplicants(){
        // 반복문으로 메시지 계속 받을 거 가틍데요 ~ 사용자 입력이 없그든여
        // 메시지 안에 내용 가꼬 와서 출력 형식만 다듬으면 됨
    }

    public static void selectApplicant(){
        //사용자 입력이 없그든여
        //이거 돌리면 내부로직 따라서 선발하고. 누가 어느호실인지 보여주거나 / 그냥 배정 완료했다고 하면됨
        //배정 호실 보여줄거면 여기에 그냥 출력 형식만~ 아니면 배정완료만
    }

    public static void ManagementMeritPoint(){
        System.out.print("관리하려는 학생의 학번을 입력하세요 : ");
        String student = sc.next();
        System.out.println("상점/벌점 입력 (벌점의 경우 음수로 입력하세요) : ");
        int point = sc.nextInt();
        //메시지 보내는 메서드
    }

    public static void ConfirmTuberReport(){
        // 결핵진단서 확인 안한 사람들 불러오는 쿼리있는 메서드
        // 한놈씩 반복문으로 조져서 그놈들이 올린 결핵진단서 메시지로 받아서 불러오는 메서드
        System.out.println("1.승인 / 2.거절 (번호를 누르세요) : ");
        // 승인거절 결과 보내는 메서드
        System.out.println("다음 학생의 결핵진단서를 불러올까요? (1. 승인 / 2. 거절) : ");
        // 값에따라 반복문 계속할지?
    }

    public static void DisplayMoveOutApplicant(){
        //메시지 보내서 퇴사신청한놈들 불러오는 메서드
        // 그놈들 환불상태 불러오는 메서드
        // 그럼 그거 보고 퇴사승인 누르기
        // 승인하면 또 메시지 보내서 학생들 상태 바꾸기
    }

    public static void ConfirmFIleForProof(){
        //위에 결핵이랑 똑같이
    }
}
