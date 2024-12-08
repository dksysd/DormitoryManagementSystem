package client.domitoryUser;

import client.domitoryAdmin.AdminPage;

import java.util.Scanner;

public class ApplicantPage {
    private static Scanner sc = new Scanner(System.in);

    public static void applicantFunction(){
        int option = 0;

        do{
            applicantFunctionInfo();
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
                default: break;
            }
        } while (option != 7);
    }

    public static void applicantFunctionInfo(){
        System.out.println("============= 학생 페이지입니다 =============");
        System.out.println("1. 선발 일정 확인");
        System.out.println("2. 입사신청하기"); // 결핵진단서는 따로 업로드 기능 만들것인지?
        System.out.println("3. 퇴사 신청 / 확인"); // 환불신청도 같이 할 것인가?
        System.out.println("4. 선발 결과 확인");
        System.out.println("5. 상벌점 확인");
        System.out.println("6. 명세서/영수증 확인");
        System.out.println("7. 로그아웃");
        System.out.println();
        System.out.println();
    }

    public static void displayInfo(){
        //사용자 입력 없음 >> 메시지 보내와서 출력만
    }

    public static void applicate(){
        // 성별 물어보는 메시지를 보내서 결과 받아올것인지? 아니면 잘못입력하면 탈락시킬지 / 탈락시킨다 가정하면
        // 메시지 보내면 서버측에서 확인하고 바로 탈락시키면 됨
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 입사 신청 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 기숙사 지망 순위 설정 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("지망하는 순서대로 기숙사 3개를 띄어쓰기로 구분하여 입력하세요 ");
        System.out.println("(a.푸름1 / b.푸름2 / c.푸름3 / d.푸름4 / " +
                "e.오름1 / f.오름2 / g.오름3 / h.아름관)");
        System.out.println();
        System.out.println(">> 예시 : e c h (1지망 - 오름1, 2지망 - 푸름3, 3지망 - 아름관)");
        System.out.println();
        System.out.println("!! 타 성별 기숙사로 잘못 입력할 경우 선발이 취소됩니다 !!");
        System.out.println();
        System.out.println(">> 지망 순위 입력 : ");
        char first = sc.next().charAt(0);
        char second = sc.next().charAt(0);
        char third = sc.next().charAt(0);
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 식사 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("신청안함(0), 5일식(5), 7일식(7) - 잘못 입력 시 푸름관의 경우 신청 안함으로 입력됩니다.");
        System.out.println("!!오름관의 경우 식사 신청이 필수입니다!! - 잘못 입력 시 5일식으로 신청됨");
        System.out.println();
        System.out.print("1지망 기숙사 식사 신청 : ");
        int meal1 = sc.nextInt();
        System.out.print("2지망 기숙사 식사 신청 : ");
        int meal2 = sc.nextInt();
        System.out.print("3지망 기숙사 식사 신청 : ");
        int meal3 = sc.nextInt();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 룸메이트 사전 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("두 사람이 모두 신청해야 ????");
        System.out.print("룸메이트 사전 신청하시겠습니까? (y/n)");
        String allow = sc.next();
        String roommate;
        if(allow.toLowerCase().equals("y")){
            System.out.println("룸메이트 하려는 학생의 학번 : ");
            roommate = sc.next();
        }


        // 결핵진단서 어케할거?
        // 날짜에 따라서 우선선발 인증 어쩌고도 넣어야함


        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        // 메시지로 보내기
        // 응답메시지 받고 결과 출력
    }

    public static void moveOutApplicate(){
        System.out.println("이용하려는 기능을 선택하세요 (1.퇴사신청 / 2.퇴사확인)");
        int option = sc.nextInt();
        if(option == 2){
            // 메시지 받아서 결과만 확인
        } else{
            // 환불신청 할거임?
        }
        // 환불 신청 같이?
    }

    public static void displaySelectionResult(){
        // 사용자 입력 없고 메시지 받기만 하면됨
    }

    public static void displayMeritPoint(){
        // 사용자 입력 없고 메시지 받기만 하면됨
    }

    public static void displayBill(){
        // 사용자 입력 없고 메시지 받기만 하면됨
    }
}
