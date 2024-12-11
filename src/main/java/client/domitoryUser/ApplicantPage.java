package client.domitoryUser;
import client.core.util.AsyncRequest;
import server.controller.PaymentController;
import shared.protocol.persistence.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ApplicantPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;

    public ApplicantPage(){}
    public ApplicantPage(String sessionID){
        this.sessionID = sessionID;
    }

    public void applicantFunction(AsyncRequest asyncRequest) throws ExecutionException, InterruptedException {
        int option = 0;

        do{
            applicantFunctionInfo();
            System.out.print("실행하려는 기능을 선택하세요 : ");
            option = sc.nextInt();
            System.out.println("=======================================");

            switch (option) {
                case 1:
                    displayInfo(asyncRequest);
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6: displayBill(asyncRequest); break;
                case 7: payment(asyncRequest); break;
                case 8:
                case 9:
                case 10:
                    System.out.println("종료합니다.");
                    break;
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 시도하세요.");
                    break;
            }
        } while (option != 8); // 8번을 선택하면 루프 종료
    }


    public static void applicantFunctionInfo(){
        System.out.println("============= 학생 페이지입니다 =============");
        System.out.println("1. 선발 일정 확인"); // 0k -  확인은 필요
        System.out.println("2. 입사신청하기"); // ok 확인 필요
        System.out.println("3. 퇴사 신청 / 확인"); // ok 확인 필요
        System.out.println("4. 선발 결과 확인"); // ok 확인 필요
        System.out.println("5. 상벌점 확인"); // ok 확인은 필요
        System.out.println("6. 명세서 확인"); // 0k
        System.out.println("7. 결제 / 결제상태 확인"); //0k
        System.out.println("8. 결핵진단서 제출 / 제출상태 확인");
        System.out.println("9. 우선선발 증빙자료 제출 / 제출상태 확인");
        System.out.println("10. 로그아웃");
        System.out.println();
        System.out.println();
    }

    //민성이가 슬쩍 보면 됨
    public void displayInfo(AsyncRequest asyncRequest) throws ExecutionException, InterruptedException, ExecutionException {
        // 선발 일정 요청 - sessionId
        Protocol<String> tlv = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0), sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_SCHEDULE,0));
        protocol.addChild(tlv);
        Protocol<?> resProtocol = asyncRequest.sendAndReceive(protocol);

        // 선발 일정 받기 - String 들로 받아와짐. -> 일정, 일정, 일정 이런 방식으로

        if(resProtocol.getHeader().getType() == Type.ERROR){
            System.out.println("잘못된 요청입니다. 재시도 하세요");
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

    public void applicate(AsyncRequest asyncRequest){
        // 성별 물어보는 요청 - 유저 인포에서 파싱으로 성별 가져옴
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_USER_INFO,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol =  asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

        boolean pureum_1, pureum_2, pureum_3;

        if(sexuality.equals("F")){
            System.out.println("(푸름3 / 오름1 )");
        } else {
            System.out.println("(푸름1 / 푸름2 / 푸름4 / 오름2 / 오름3)");
        }
        System.out.println();
        System.out.println(">> 예시 : e c h (1지망 - 오름1, 2지망 - 푸름3, 3지망 - 아름관)");
        System.out.println();
        System.out.println("!! 타 성별 기숙사로 잘못 입력할 경우 선발이 취소됩니다 !!");
        System.out.println();
        System.out.println(">> 지망 순위 입력 : ");
        String first = sc.next();
        String second = sc.next();
        String third = sc.next();
        String domi1, domi2, domi3;

        if(sexuality.equals("F")){
            pureum_1 = (first.equals("푸름3"));
            pureum_2 = (second.equals("푸름3"));
            pureum_3 = (third.equals("푸름3"));

        } else {
            pureum_1 = (!first.equals("오름2") && !first.equals("오름3"));
            pureum_2 = (!second.equals("오름2") && !second.equals("오름3"));
            pureum_3 = (!third.equals("오름2") && !third.equals("오름3"));
        }

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 식사 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("신청안함, 5일식, 7일식 - 잘못 입력 시 푸름관의 경우 신청 안함으로 입력됩니다.");
        System.out.println("!!오름관의 경우 식사 신청(5일식 또는 7일식)이 필수입니다!! - 잘못 입력 시 5일식으로 신청됨");
        System.out.println();

        System.out.print("1지망 기숙사 식사 신청 : ");
        String meal1 = sc.next();
        if(pureum_1){
            if(!(meal1.equals("신청안함") || meal1.equals("5일식") || meal1.equals("7일식"))){
                meal1 = "신청안함";
            }
        } else {
            if(!(meal1.equals("5일식") || meal1.equals("7일식"))){
                meal1 = "5일식";
            }
        }

        System.out.print("2지망 기숙사 식사 신청 : ");
        String meal2 = sc.next();
        if(pureum_2){
            if(!(meal2.equals("신청안함") || meal2.equals("5일식") || meal2.equals("7일식"))){
                meal2 = "신청안함";
            }
        } else {
            if(!(meal2.equals("5일식") || meal2.equals("7일식"))){
                meal2 = "5일식";
            }
        }

        System.out.print("3지망 기숙사 식사 신청 : ");
        String meal3 = sc.next();
        if(pureum_3){
            if(!(meal3.equals("신청안함") || meal3.equals("5일식") || meal3.equals("7일식"))){
                meal3 = "신청안함";
            }
        } else {
            if(!(meal3.equals("5일식") || meal3.equals("7일식"))){
                meal3 = "5일식";
            }
        }

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 룸메이트 사전 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("두 사람이 모두 신청해야 적용됩니다");
        System.out.print("룸메이트 사전 신청하시겠습니까? (y/n)");
        String roommateAllow = sc.next();
        String roommate = "";
        boolean haveRoommate = false;
        if(roommateAllow.toLowerCase().equals("y")){
            System.out.println("룸메이트 하려는 학생의 학번 : ");
            roommate = sc.next();
            haveRoommate = true;
        }

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 특이사항 기재 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.print("1년 호실 신청하시겠습니까? 잘못 입력 시 한 학기만 신쳥됩니다 (y/n)");
        String temp = sc.next();
        boolean oneYear = false;
        if(temp.toLowerCase().equals("y")){
            oneYear = true;
        }
        System.out.print("잠버릇 여부를 체크해주세요. 잘못 입력 시 잠버릇 있는 것으로 간주됩니다. (y/n)");
        temp = sc.next();
        boolean snore = true;
        if(temp.toLowerCase().equals("n")){
            snore = false;
        }
        //선호도 -> 잠버릇(bool) -> 1년호실 (bool) -> 어디 기숙사인지 -> 밥-> 룸메이트
        Protocol<Boolean> yearlast = new Protocol<>(new Header(Type.VALUE, DataType.BOOLEAN, Code.ValueCode.ONEYEAR_LASTING,0), oneYear);
        Protocol<Boolean> snoring = new Protocol<>(new Header(Type.VALUE, DataType.BOOLEAN, Code.ValueCode.SNORE,0), snore);
        Protocol<String> roommates;
        Protocol<String> session = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0), sessionID);



        Protocol<?> protocol1 = new Protocol<>();
        Header header1 = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.APPLICATION,0);
        Protocol<Integer> prefer1 = new Protocol<>(new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.PREFERENCE,0), 1);
        Protocol<String> domitory1 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE,0), first);
        Protocol<String> mealSchedule1 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.MEAL_PLAN,0), meal1);
        protocol1.setHeader(header1);
        protocol1.addChild(prefer1); protocol1.addChild(snoring); protocol1.addChild(yearlast); protocol1.addChild(domitory1);
        protocol1.addChild(mealSchedule1);

        Protocol<?> protocol2 = new Protocol<>();
        Header header2 = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.APPLICATION,0);
        Protocol<Integer> prefer2 = new Protocol<>(new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.PREFERENCE,0), 2);
        Protocol<String> domitory2 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE,0), second);
        Protocol<String> mealSchedule2 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.MEAL_PLAN,0), meal2);
        protocol2.setHeader(header2);
        protocol2.addChild(prefer2); protocol1.addChild(snoring); protocol1.addChild(yearlast); protocol1.addChild(domitory2);
        protocol1.addChild(mealSchedule2);


        Protocol<?> protocol3 = new Protocol<>();
        Header header3 = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.APPLICATION,0);
        Protocol<Integer> prefer3 = new Protocol<>(new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.PREFERENCE,0), 3);
        Protocol<String> domitory3 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE,0), third);
        Protocol<String> mealSchedule3 = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.MEAL_PLAN,0), meal3);
        protocol1.setHeader(header3);
        protocol1.addChild(prefer3); protocol1.addChild(snoring); protocol1.addChild(yearlast); protocol1.addChild(domitory3);
        protocol1.addChild(mealSchedule3);

        if(haveRoommate){
            roommates = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE,0), roommate);
            protocol1.addChild(roommates);
            protocol2.addChild(roommates);
            protocol3.addChild(roommates);
        }

        protocol1.addChild(session);
        protocol2.addChild(session);
        protocol3.addChild(session);


        Protocol<?> resProtocol1, resProtocol2, resProtocol3;
        try {
            resProtocol1 =  asyncRequest.sendAndReceive(protocol1);
            resProtocol2 =  asyncRequest.sendAndReceive(protocol2);
            resProtocol3 =  asyncRequest.sendAndReceive(protocol3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if((resProtocol1.getHeader().getCode() != Code.ResponseCode.OK) || (resProtocol2.getHeader().getCode() != Code.ResponseCode.OK) || (resProtocol3.getHeader().getCode() != Code.ResponseCode.OK) ){
            System.out.println("재시도해주세요");
            return;
        }

        System.out.println("입사신청서 등록이 완료되었습니다. 결핵진단서를 제출하지 않으면 합격이 취소되며, \n우선선발의 경우 결핵진단서에 더하여 우선선발 증빙자료를 제출하지 않으면 합격이 취소됩니다");
        System.out.println("결핵진단서와 우선선발 증빙자료 제출은 각각 학생페이지 9번, 10번 기능입니다.");

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        // 메시지로 보내기
        // 응답메시지 받고 결과 출력

    }

    // 서현이가 봐죠 - 퇴사신청(환불신청) 부분
    // 퇴사 확인 부문만 추가하면 됨!
    public void moveOutApplicate(AsyncRequest asyncRequest){
        System.out.println("이용하려는 기능을 선택하세요 (1.퇴사신청 / 2.퇴사확인)");
        int option = sc.nextInt();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        if(option == 2){
            // 퇴사확인 요청
            Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.CHECK_MOVE_OUT,0);
            Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
            Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
            Protocol<?> protocol = new Protocol<>();
            protocol.setHeader(header);
            protocol.addChild(tlv);

            Protocol<?> resProtocol;
            try {
                resProtocol =  asyncRequest.sendAndReceive(protocol);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
                String status = (String) resProtocol.getChildren().getFirst().getData();
                System.out.println(status);
            }

        } else{
            // 환불신청 > 결과 확인은 학생 페이지 7번 결제확인 기능을 사용하라고 안내
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
                    resProtocol =  asyncRequest.sendAndReceive(protocol);
                } catch (Exception e) {
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
    public void displaySelectionResult(AsyncRequest asyncRequest){
        // 합격했는지 확인 요청
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_RESULT,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol =  asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(resProtocol.getHeader().getCode() != Code.ResponseCode.OK){
            System.out.println("재시도해주세요");
            return;
        }

        int size = resProtocol.getChildren().size();
        String text;
        for(int i = 0; i < size; i++){
            text = (String) resProtocol.getChildren().get(i).getData();
            System.out.println(text + "--");
        }

    }

    public void displayMeritPoint(AsyncRequest asyncRequest){
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
            resProtocol =  asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
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

    public void displayBill(AsyncRequest asyncRequest){
        // 사용자 입력 없고 메시지 받기만 하면됨
        //클래스 및 메서드 불러오기
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.BILL,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol =  asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
            int value = (int) resProtocol.getChildren().get(0).getData();;
            System.out.println("납부해야할 금액 : " + value + "원입니다.");

        } else{
            System.out.println("명세서 불러오기 오류!");
        }
    }

    //서혀나 봐조
    public void payment(AsyncRequest asyncRequest){
        //Payment 클래스 끌어와서 하기
        System.out.println("하려는 기능을 선택하세요. (1. 결제상태 확인 / 2. 결제하기)");
        int selection = sc.nextInt();
        if(selection == 1){
            paidCheck(asyncRequest);
        }
        if(selection==2 && !paidCheck(asyncRequest)){
            displayBill(asyncRequest);
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

            Protocol<?> protocol = new Protocol<>();
            Protocol<?> resProtocol;
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
                    resProtocol =  asyncRequest.sendAndReceive(protocol);
                } catch (Exception e) {
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

    public boolean paidCheck(AsyncRequest asyncRequest){
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT_CHECK,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol =  asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
            return true;
        } else if(resProtocol.getHeader().getType() == Type.ERROR){
            Code code = resProtocol.getHeader().getCode();
            if(code == Code.ErrorCode.INVALID_REQUEST){
                System.out.println("미납 상태입니다.");
            } else{
                System.out.println("세션 만료. 재로그인하세요.");
            }
        }

        return false;
    }
}
