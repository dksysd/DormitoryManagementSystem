package client.domitoryUser;
import server.controller.PaymentController;
import shared.protocol.persistence.*;


import java.util.List;
import java.util.Scanner;

public class ApplicantPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;
    public void applicantFunction(){
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
        System.out.println("3. 퇴사 신청 / 확인"); // 환불신청도 같이 할 것인가? >> 환불신청도 같이
        System.out.println("4. 선발 결과 확인");
        System.out.println("5. 상벌점 확인");
        System.out.println("6. 명세서/영수증 확인");
        System.out.println("7. 결제 / 결제상태 확인");
        System.out.println("8. 로그아웃");
        System.out.println();
        System.out.println();
    }

    public void displayInfo(){
        // 선발 일정 요청 - sessionId
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.SELECTION_SCHEDULE,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        // 선발 일정 받기 - String 들로 받아와짐. -> 일정, 일정, 일정 이런 방식으로
    }

    public void applicate(){
        // 성별 물어보는 요청 - 유저 인포에서 파싱으로 성별 가져옴 -> User getUserInfo로 가시면 될듯
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_USER_INFO,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);
        // 성별 받아오기 -> getUser Info 참조하시면 됨.

        // 성별별로 좀 다르게 하기 - 수정 필요
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


        // 결핵진단서 어케할거? -> setTuber 머시기 만들 예정.
        // 날짜에 따라서 우선선발 인증 어쩌고도 넣어야함


        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        // 메시지로 보내기
        // 응답메시지 받고 결과 출력
    }

    public void moveOutApplicate(){
        System.out.println("이용하려는 기능을 선택하세요 (1.퇴사신청 / 2.퇴사확인)");
        int option = sc.nextInt();
        if(option == 2){
            // 퇴사확인 요청
            Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.CHECK_MOVE_OUT,0);
            Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
            Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
            Protocol<?> protocol = new Protocol<>();
            protocol.setHeader(header);
            protocol.addChild(tlv);

            // 메시지 받아서 결과만 확인

        } else{
            // 환불신청 >> 결과 확인은 학생 페이지 7번 결제확인 기능을 사용하라고 안내
            System.out.println("환불 완료 시, 자동으로 퇴사 신청이 됩니다. \"환불\" 입력 시 환불이 완료됩니다. : ");
            String text = sc.next();

            if(text.trim().equals("환불")){
                // 환불 요청 보내기
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.MOVE_OUT,0);
                Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
                Protocol<?> protocol = new Protocol<>();
                protocol.setHeader(header);
                protocol.addChild(tlv);

                // 메시지 받고 환불 완료 안내 띄우기

            }
        }
    }

    public void displaySelectionResult(){
        // 합격했는지 확인 요청
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_RESULT,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        //날짜가 안됐다면 오류 메시지가 옴 - 그거 처리

    }

    public void displayMeritPoint(){
        //상벌점 요청
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_MERIT_AND_DEMERIT_POINTS,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        //상벌점 불러오기

        int point = 0;
        System.out.println("총 점수 : " + point);
        System.out.println("자세한 사항은 관리자에게 문의하세요.");
    }

    public void displayBill(){
        // 사용자 입력 없고 메시지 받기만 하면됨
        //클래스 및 메서드 불러오기
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.BILL,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resultProtocol;
        Type resType;

        try {
            resultProtocol = PaymentController.getPaymentAmount(protocol);
            resType = resultProtocol.getHeader().getType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(resType == Type.RESPONSE){

            List<Protocol<?>> list = resultProtocol.getChildren();
            Protocol<?> childProtocol = list.getFirst();
            int value = (int) childProtocol.getData();
      //      int value = (int) resultProtocol.getChildren().getFirst().getData();
            System.out.println("납부해야할 금액 : " + value + "원입니다.");

        } else{
            System.out.println("명세서 불러오기 오류!");
        }
    }

    public void displayReceipt(){
        //클래스 및 메서드 불러오기
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_RECEIPT,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        String price = null;

        displayBill();
        //결제상태 확인
        if(paidCheck()){
            System.out.println("납부된 금액 : " + price + "원입니다.");
        }
    }

    public void payment(){
        //Payment 클래스 끌어와서 하기
        if(!paidCheck()){
            displayBill();
            System.out.println("납부 후, \"납부\" 글자를 입력해주시면 결제상태가 완료로 바뀝니다 : ");
            String text = sc.next();
            if(text.trim().equals("납부")){
                // 납부 상태 변경 요청
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT_AMOUNT,0);
                Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
                Protocol<?> protocol = new Protocol<>();
                protocol.setHeader(header);
                protocol.addChild(tlv);

                // 응답 메시지로 할건지 ? 페이드체크 메서드 호출로 완료 안내줄건지?
            }
        }

    }

    public boolean paidCheck(){
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT_CHECK,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);
        //
        // 결제 상태 따라서 불리언 값 리턴
        return false;
    }
}
