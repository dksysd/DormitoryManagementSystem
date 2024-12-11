package client.domitoryUser;
import server.controller.DormitoryUserController;
import server.controller.PaymentController;
import server.controller.UserController;
import shared.protocol.persistence.*;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ApplicantPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;

    public ApplicantPage(){}
    public ApplicantPage(String sessionID){
        this.sessionID = sessionID;
    }

    public void applicantFunction(){
        int option = 0;

        do{
            applicantFunctionInfo();
            System.out.print("실행하려는 기능을 선택하세요 : ");
            option = sc.nextInt();
            System.out.println("=======================================");

            switch (option){
                case 1: displayInfo(); break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6: displayBill(); break;
                case 7: payment(); break;
                case 8:
                default: break;
            }
        } while (option != 7);
    }

    public static void applicantFunctionInfo(){
        System.out.println("============= 학생 페이지입니다 =============");
        System.out.println("1. 선발 일정 확인"); // 0k -  확인은 필요
        System.out.println("2. 입사신청하기"); // 손도 안댐 //
        System.out.println("3. 퇴사 신청 / 확인"); // 퇴사 확인 기능 추가 필요 //
        System.out.println("4. 선발 결과 확인"); // 손도 안댐 //
        System.out.println("5. 상벌점 확인"); // ok 확인은 필요
        System.out.println("6. 명세서 확인"); // 0k
        System.out.println("7. 결제 / 결제상태 확인"); //0k
        System.out.println("8. 로그아웃");
        System.out.println();
        System.out.println();
    }

    //민성이가 슬쩍 보면 됨
    public void displayInfo(){
        // 선발 일정 요청 - sessionId
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_SCHEDULE,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        // 선발 일정 받기 - String 들로 받아와짐. -> 일정, 일정, 일정 이런 방식으로
        Protocol<?> resProtocol = null;
        try {
            resProtocol = DormitoryUserController.getSelectionSchedule(protocol);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(resProtocol.getHeader().getType() == Type.ERROR){
            System.out.println("재시도 하세요");
            return;
        }

        int size = resProtocol.getChildren().size() ;

        String priority = "미정", normal = "미정", latest = "미정", temp, date, name;

        for(int i = 0; i < size; i++){
            temp = (String) resProtocol.getChildren().get(i).getData();
            date = temp.substring(7);
            name = temp.substring(0,3);

            if(name.equals("우선선발"))
                priority = date;
            else if(name.equals("일반선발")){
                normal = date;
            }
            else
                latest = date;
        }

        System.out.println("우선선발과 일반선발은 14일간 진행됩니다.");
        System.out.println("우선선발 시작일 : " + priority);
        System.out.println("일반선발 시작일 : " + normal);
        System.out.println("\n 추가선발은 선착순으로 완료되며, 공실이 생길때마다 진행됩니다.");
        System.out.println("추가선발 : " + latest);
    }

    public void applicate(){
        // 성별 물어보는 요청 - 유저 인포에서 파싱으로 성별 가져옴
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_USER_INFO,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol = UserController.getUserInfo(protocol);
        String sexuality = null;
        if(resProtocol.getHeader().getType() == Type.RESPONSE){
            sexuality = (String) resProtocol.getChildren().get(3).getData();;
        } else {
            System.out.println("학생 정보를 가져올 수 없습니다. 재로그인 해주세요");
            return;
        }

        // 성별별로 좀 다르게 하기 - 수정 필요 + 식사신청이랑 묶어서 받는지? 따로 받는지?
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 입사 신청 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 기숙사 지망 순위 설정 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("지망하는 순서대로 기숙사 3개를 띄어쓰기로 구분하여 입력하세요 ");

        if(sexuality.equals("F")){
            System.out.println("(a.푸름3 / b.오름1 / c.아름관)");
        } else {
            System.out.println("(a.푸름1 / b.푸름2 / c.푸름4 / d.오름2 / e.오름3");
        }
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

    // 서현이가 봐죠 - 퇴사신청(환불신청) 부분
    // 퇴사 확인 부문만 추가하면 됨!
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
            System.out.println("환불 완료 시, 자동으로 퇴사 신청이 됩니다. \"환불\" 입력 시 환불절차가 시작됩니다. : ");
            String text = sc.next();
            System.out.println("환불 받을 계좌번호, 계좌주 이름, 은행명을 띄어쓰기로 구분하여 입력해주세요 : ");
            String account = sc.next();
            String name = sc.next();
            String bank = sc.next();

            if(text.trim().equals("환불")){
                // 환불 요청 보내기
                Protocol<?> protocol = new Protocol<>();
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.REFUND_REQUEST,0);
                protocol.setHeader(header);

                for(int i = 0; i < 5; i++){
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    switch (i){
                        case 0: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, text);
                            break;
                        case 1: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_NUMBER, 0);
                            tlv = new Protocol<>(tlvHeader, account);
                            break;
                        case 2: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_HOLDER_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, name);
                            break;
                        case 3: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.BANK_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, bank);
                            break;
                        case 4: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            tlv = new Protocol<>(tlvHeader, sessionID);
                            break;
                    }

                    protocol.addChild(tlv);
                }

                Protocol<?> resProtocol;
                try {
                    resProtocol = PaymentController.requestRefund(protocol);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                // 메시지 받고 환불 완료 안내 띄우기 - 여기 좀 봐죠!!!!! 응답메시지 어떻게 오는지 몰라서!! 안에 밸류는 필요 없을 것 같아서 이러케 함
                if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
                    System.out.println("환불과 퇴사신청이 정상적으로 완료되었습니다.");
                    System.out.println("환불 상태를 다시 확인하고 싶으시다면 학생페이지 7번 결제 상태 확인을 참고 하세요.");
                }

            }
        }
    }

    //민성이가 확인 좀 해주셈
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
        Protocol<?> resProtocol;
        try {
            resProtocol = DormitoryUserController.getMeritAndDemeritPoints(protocol);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(resProtocol.getHeader().getCode() != Code.ResponseCode.OK){
            System.out.println("상벌점 내역을 불러올 수 없습니다. 재시도 하세요.");
            return;
        }

        int size = resProtocol.getChildren().size();
        int strSize;
        String temp, reason, point;
        for(int i = 0; i < size; i++){
            temp = (String) resProtocol.getChildren().get(i).getData();
            strSize = temp.length();
            reason = temp.substring(0, strSize - 2);
            point = temp.substring(strSize - 1);
            System.out.println("상벌점 내역 : " + reason + " / " + point);
        }
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
            int value = (int) resultProtocol.getChildren().get(0).getData();;
            System.out.println("납부해야할 금액 : " + value + "원입니다.");

        } else{
            System.out.println("명세서 불러오기 오류!");
        }
    }

    //서혀나 봐조
    public void payment(){
        //Payment 클래스 끌어와서 하기
        System.out.println("하려는 기능을 선택하세요. (1. 결제상태 확인 / 2. 결제하기)");
        int selection = sc.nextInt();
        if(selection == 1){
            paidCheck();
        }
        if(selection==2 && !paidCheck()){
            displayBill();
            System.out.println("납부 방법을 선택하세요 (1.계좌이체 / 2.카드결제)");
            selection = sc.nextInt();
            String account = null, name = null, bank = null, cardNum = null, cardCompany = null;
            if(selection == 1){
                System.out.println("보내는 이의 계좌번호, 계좌주이름, 은행명을 띄어쓰기로 구분하여 입력하세요");
                account = sc.next();
                name = sc.next();
                bank = sc.next();
            } else if(selection == 2){
                System.out.println("카드번호, 카드사를 띄어쓰기로 구분하여 입력하세요");
                cardNum = sc.next();
                cardCompany = sc.next();
            } else {
                return;
            }
            System.out.println("납부 후, \"납부\" 글자를 입력해주시면 결제상태가 완료로 바뀝니다 : ");
            String text = sc.next();

            if(!text.trim().equals("납부")){
                return;
            }

            Protocol<?> resProtocol = null;
            Protocol<?> protocol = new Protocol<>();
            if(selection == 1){
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.BANK_TRANSFER,0);
                protocol.setHeader(header);

                for(int i = 0; i < 5; i++){
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    switch (i){
                        case 0: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_NUMBER, 0);
                            tlv = new Protocol<>(tlvHeader, text);
                            break;
                        case 1: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_HOLDER_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, account);
                            break;
                        case 2: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.BANK_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, name);
                            break;
                        case 3: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, bank);
                            break;
                        case 4: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            tlv = new Protocol<>(tlvHeader, sessionID);
                            break;
                    }

                    protocol.addChild(tlv);
                }
                try {
                    resProtocol = PaymentController.payByBankTransfer(protocol);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.CARD_MOVEMENT,0);
                protocol.setHeader(header);

                for(int i = 0; i < 4; i++){
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    switch (i){
                        case 0: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.CARE_NUMBER, 0);
                            tlv = new Protocol<>(tlvHeader, cardNum);
                            break;
                        case 1: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.CARD_ISSUER, 0);
                            tlv = new Protocol<>(tlvHeader, cardCompany);
                            break;
                        case 2: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, text);
                            break;
                        case 3: tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            tlv = new Protocol<>(tlvHeader, sessionID);
                            break;
                    }

                    protocol.addChild(tlv);
                }
                resProtocol = PaymentController.payByCard(protocol);
            }

            if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
                System.out.println("정상적으로 납부되었습니다.");
            } else {
                System.out.println("결제가 완료되지 않았습니다. 다시 시도해주세요.");
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

        Protocol<?> resultProtocol;
        Type resType;

        try {
            resultProtocol = PaymentController.getPaymentStatus(protocol);
            resType = resultProtocol.getHeader().getType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(resType == Type.RESPONSE){
            return true;
        } else if(resType == Type.ERROR){
            Code code = resultProtocol.getHeader().getCode();
            if(code == Code.ResponseCode.ErrorCode.INVALID_REQUEST){
                System.out.println("미납 상태입니다.");
            } else{
                System.out.println("세션 만료. 재로그인하세요.");
            }
        }

        return false;
    }
}
