package client.domitoryUser;

import client.core.util.AsyncRequest;
import server.controller.PaymentController;
import shared.protocol.persistence.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ApplicantPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;

    public ApplicantPage() {
    }

    public ApplicantPage(String sessionID) {
        this.sessionID = sessionID;
    }

    public void applicantFunction(AsyncRequest asyncRequest) throws ExecutionException, InterruptedException {
        int option = 0;

        do {
            applicantFunctionInfo();
            System.out.print("실행하려는 기능을 선택하세요 : ");
            option = sc.nextInt();
            System.out.println("=======================================");

            switch (option) {
                case 1:
                    displayInfo(asyncRequest);
                    break;
                case 2:
                    applicate(asyncRequest);
                    break;
                case 3:
                    moveOutApplicate(asyncRequest);
                    break;
                case 4:
                    displaySelectionResult(asyncRequest);
                    break;
                case 5:
                    displayMeritPoint(asyncRequest);
                    break;
                case 6:
                    displayBill(asyncRequest);
                    break;
                case 7:
                    payment(asyncRequest);
                    break;
                case 8:
                    appliyTuber(asyncRequest);
                    break;
                case 9:
                case 10:
                    System.out.println("종료합니다.");
                    break;
                default:
                    System.out.println("유효하지 않은 선택입니다. 다시 시도하세요.");
                    break;
            }
        } while (option != 10); // 8번을 선택하면 루프 종료
    }


    public static void applicantFunctionInfo() {
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


    public void displayInfo(AsyncRequest asyncRequest) throws InterruptedException, ExecutionException {
        // 선발 일정 요청 - sessionId
        Protocol<String> tlv = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0), sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_SCHEDULE, 0));
        protocol.addChild(tlv);
        Protocol<?> resProtocol = asyncRequest.sendAndReceive(protocol);


        if (resProtocol.getHeader().getType() == Type.ERROR) {
            System.out.println("잘못된 요청입니다. 재시도 하세요");
            return;
        }
        String priority ="";
        String normal = "";
        String extra = "";
        String first = (String) resProtocol.getChildren().getFirst().getData();
        String second = (String) resProtocol.getChildren().get(1).getData();
        String third = (String) resProtocol.getChildren().getLast().getData();

        String[] firstParts = first.split("\\s+");
        String[] secondParts = second.split("\\s+");
        String[] thirdParts = third.split("\\s+");

        if (firstParts.length > 0) {
            if (firstParts[0].equals("우선선발")) {
                priority = first;
            } else if (firstParts[0].equals("일반선발")) {
                normal = first;
            } else {
                extra = first;
            }
        }

        if (secondParts.length > 0) {
            if (secondParts[0].equals("우선선발")) {
                priority = second;
            } else if (secondParts[0].equals("일반선발")) {
                normal = second;
            } else {
                extra = second;
            }
        }

        if (thirdParts.length > 0) {
            if (thirdParts[0].equals("우선선발")) {
                priority = third;
            } else if (thirdParts[0].equals("일반선발")) {
                normal = third;
            } else {
                extra = third;
            }
        }

        printSchedule(priority);
        printSchedule(normal);
        printSchedule(extra);


        System.out.println("\n 추가선발은 선착순으로 완료되는 점 유의 바랍니다.");

    }

    public void applicate(AsyncRequest asyncRequest) {
        // 요청 준비
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_USER_INFO, 0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        // 응답 처리
        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getType() != Type.RESPONSE) {
            System.out.println("학생 정보를 가져올 수 없습니다. 재로그인 해주세요");
            return;
        }
        String sexuality = (String) resProtocol.getChildren().get(2).getData();

        // 입사 신청 입력
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 입사 신청 페이지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 기숙사 지망 순위 설정 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("지망하는 순서대로 기숙사 3개를 띄어쓰기로 구분하여 입력하세요 ");

        if (sexuality.equals("female")) {
            System.out.println("( 푸름1 / 푸름3 / 오름1 )");
        } else {
            System.out.println("( 푸름2 / 푸름4 / 오름2 )");
        }
        System.out.println(">> 예시 : 오름1 푸름3 푸름1 (1지망 - 오름1, 2지망 - 푸름3, 3지망 - 푸름3)");
        System.out.println();
        System.out.print(">> 지망 순위 입력 : ");

        String first = sc.next();
        String second = sc.next();
        String third = sc.next();

        boolean[] pureum = new boolean[3];
        String[] dormitories = {first, second, third};
        for (int i = 0; i < 3; i++) {
            if (sexuality.equals("female")) {
                pureum[i] = !dormitories[i].equals("오름1");
            } else {
                pureum[i] = !(dormitories[i].equals("오름2") || dormitories[i].equals("오름3"));
            }
        }

        // 식사 신청 입력
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 식사 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("신청안함, 5일식, 7일식 - 잘못 입력 시 푸름관의 경우 신청 안함으로 입력됩니다.");
        System.out.println("!!오름관의 경우 식사 신청(5일식 또는 7일식)이 필수입니다!! - 잘못 입력 시 5일식으로 신청됨");
        System.out.println();

        String[] meals = new String[3];
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + "지망 기숙사 식사 신청 : ");
            meals[i] = sc.next();
            if (pureum[i]) {
                if (!(meals[i].equals("신청안함") || meals[i].equals("5일식") || meals[i].equals("7일식"))) {
                    meals[i] = "신청안함";
                }
            } else {
                if (!(meals[i].equals("5일식") || meals[i].equals("7일식"))) {
                    meals[i] = "5일식";
                }
            }
        }

        // 룸메이트 신청
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 룸메이트 사전 신청 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("두 사람이 모두 신청해야 적용됩니다");
        System.out.print("룸메이트 사전 신청하시겠습니까? (y/n) : ");
        boolean haveRoommate = sc.next().equalsIgnoreCase("y");
        String roommate = haveRoommate ? sc.next() : "";

        // 특이사항 기재
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 특이사항 기재 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.print("1년 호실 신청하시겠습니까? 잘못 입력 시 한 학기만 신쳥됩니다 (y/n) : ");
        boolean oneYear = sc.next().equalsIgnoreCase("y");
        System.out.print("잠버릇 여부를 체크해주세요. 잘못 입력 시 잠버릇 있는 것으로 간주됩니다. (y/n) : ");
        boolean snore = !sc.next().equalsIgnoreCase("n");

        // 프로토콜 생성 및 전송
        Protocol<Boolean> yearlast = new Protocol<>(new Header(Type.VALUE, DataType.BOOLEAN, Code.ValueCode.ONEYEAR_LASTING, 0), oneYear);
        Protocol<Boolean> snoring = new Protocol<>(new Header(Type.VALUE, DataType.BOOLEAN, Code.ValueCode.SNORE, 0), snore);
        Protocol<String> session = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0), sessionID);
        Protocol<String> roommates = haveRoommate ? new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE, 0), roommate) : null;

        for (int i = 0; i < 3; i++) {
            Protocol<?> protocolN = new Protocol<>();
            protocolN.setHeader(new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.APPLICATION, 0));
            protocolN.addChild(new Protocol<>(new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.PREFERENCE, 0), i + 1));
            protocolN.addChild(snoring);
            protocolN.addChild(yearlast);
            protocolN.addChild(new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.DORMITORY_ROOM_TYPE, 0), dormitories[i]));
            protocolN.addChild(new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.MEAL_PLAN, 0), meals[i]));
            if (haveRoommate) protocolN.addChild(roommates);
            protocolN.addChild(session);

            try {
                Protocol<?> resProtocolN = asyncRequest.sendAndReceive(protocolN);
                if (resProtocolN.getHeader().getCode() != Code.ResponseCode.OK) {
                    System.out.println((i + 1) + "순위 전송 오류");
                    return;
                }
            } catch (Exception e) {
                System.out.println((i + 1) + "순위 전송 오류");
                throw new RuntimeException(e);
            }
        }

        // 완료 메시지
        System.out.println("입사신청서 등록이 완료되었습니다. 결핵진단서를 제출하지 않으면 합격이 취소되며, ");
        System.out.println("우선선발의 경우 결핵진단서에 더하여 우선선발 증빙자료를 제출하지 않으면 합격이 취소됩니다");
        System.out.println("결핵진단서와 우선선발 증빙자료 제출은 각각 학생페이지 9번, 10번 기능입니다.");
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
    }


    public void moveOutApplicate(AsyncRequest asyncRequest) {
        System.out.println("이용하려는 기능을 선택하세요 (1.퇴사신청 / 2.퇴사확인)");
        int option = sc.nextInt();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        if (option == 2) {
            // 퇴사확인 요청
            Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.CHECK_MOVE_OUT, 0);
            Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
            Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
            Protocol<?> protocol = new Protocol<>();
            protocol.setHeader(header);
            protocol.addChild(tlv);

            Protocol<?> resProtocol;
            try {
                resProtocol = asyncRequest.sendAndReceive(protocol);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
                String status = (String) resProtocol.getChildren().getFirst().getData();
                System.out.println(status);
            }

        } else {
            // 환불신청 > 결과 확인은 학생 페이지 7번 결제확인 기능을 사용하라고 안내
            System.out.println("환불 완료 시, 자동으로 퇴사 신청이 됩니다. \"환불\" 입력 시 환불절차가 시작됩니다. : ");
            String text = sc.next();
            System.out.println("환불 사유를 말씀해 주세요.");
            String reason = sc.next();
            System.out.println("환불 받을 계좌번호, 계좌주 이름, 은행명을 띄어쓰기로 구분하여 입력해주세요 : ");
            String account = sc.next();
            String name = sc.next();
            String bank = sc.next();

            if (text.trim().equals("환불")) {
                // 환불 요청 보내기
                Protocol<?> protocol = new Protocol<>();
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.REFUND_REQUEST, 0);
                protocol.setHeader(header);

                for (int i = 0; i < 5; i++) {
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    tlv = switch (i) {
                        case 0 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            yield new Protocol<>(tlvHeader, text);
                        }
                        case 1 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.REFUND_REASON, 0);
                            yield new Protocol<>(tlvHeader, reason);
                        }
                        case 2 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_NUMBER, 0);
                            yield new Protocol<>(tlvHeader, account);
                        }
                        case 3 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_HOLDER_NAME, 0);
                            yield new Protocol<>(tlvHeader, name);
                        }
                        case 4 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.BANK_NAME, 0);
                            yield new Protocol<>(tlvHeader, bank);
                        }
                        case 5 -> {
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            yield new Protocol<>(tlvHeader, sessionID);
                        }
                        default -> tlv;
                    };

                    protocol.addChild(tlv);
                }

                Protocol<?> resProtocol;
                try {
                    resProtocol = asyncRequest.sendAndReceive(protocol);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // 메시지 받고 환불 완료 안내 띄우기
                if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
                    System.out.println("환불과 퇴사신청이 정상적으로 완료되었습니다.");
                    System.out.println("환불 상태를 다시 확인하고 싶으시다면 학생페이지 7번 결제 상태 확인을 참고 하세요.");
                }else{
                    System.out.println("환불 및 퇴사신청이 비정상적으로 종료되었습니다. 다시 시도해 주세요.");

                }

            }
        }
    }

    public void displaySelectionResult(AsyncRequest asyncRequest) {
        // 합격했는지 확인 요청
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_RESULT, 0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() != Code.ResponseCode.OK) {
            System.out.println("재시도해주세요");
            return;
        }

        int size = resProtocol.getChildren().size();
        String text;
        for (int i = 0; i < size; i++) {
            text = (String) resProtocol.getChildren().get(i).getData();
            System.out.println(text + "--");
        }

    }

    public void displayMeritPoint(AsyncRequest asyncRequest) {
        //상벌점 요청
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_MERIT_AND_DEMERIT_POINTS, 0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        //상벌점 불러오기
        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() != Code.ResponseCode.OK) {
            System.out.println("상벌점 내역을 불러올 수 없습니다. 재시도 하세요.");
            return;
        }

        int size = resProtocol.getChildren().size();
        int strSize;
        String temp, reason, point;
        for (int i = 0; i < size; i++) {
            temp = (String) resProtocol.getChildren().get(i).getData();
            strSize = temp.length();
            reason = temp.substring(0, strSize - 2);
            point = temp.substring(strSize - 1);
            System.out.println("상벌점 내역 : " + reason + " / " + point);
        }
        System.out.println("자세한 사항은 관리자에게 문의하세요.");
    }

    public void displayBill(AsyncRequest asyncRequest) {
        //클래스 및 메서드 불러오기
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.BILL, 0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
            int value = (int) resProtocol.getChildren().get(0).getData();
            ;
            System.out.println("납부해야할 금액 : " + value + "원입니다.");

        } else {
            System.out.println("명세서 불러오기 오류!");
        }
    }


    public void payment(AsyncRequest asyncRequest) {
        //결제 상태 확인, 결제하기
        System.out.println("하려는 기능을 선택하세요. (1. 결제상태 확인 / 2. 결제하기)");
        int selection = sc.nextInt();
        if (selection == 1) {
            paidCheck(asyncRequest);
        }
        if (selection == 2 && !paidCheck(asyncRequest)) {
            displayBill(asyncRequest);
            System.out.println("납부 방법을 선택하세요 (1.계좌이체 / 2.카드결제)");
            selection = sc.nextInt();
            String account = null, name = null, bank = null, cardNum = null, cardCompany = null;
            if (selection == 1) {
                System.out.println("보내는 이의 계좌번호, 계좌주이름, 은행명을 띄어쓰기로 구분하여 입력하세요");
                account = sc.next();
                name = sc.next();
                bank = sc.next();
            } else if (selection == 2) {
                System.out.println("카드번호, 카드사를 띄어쓰기로 구분하여 입력하세요");
                cardNum = sc.next();
                cardCompany = sc.next();
            } else {
                return;
            }
            System.out.println("납부 후, \"납부\" 글자를 입력해주시면 결제상태가 완료로 바뀝니다 : ");
            String text = sc.next();

            if (!text.trim().equals("납부")) {
                return;
            }

            Protocol<?> protocol = new Protocol<>();
            Protocol<?> resProtocol;
            if (selection == 1) {
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.BANK_TRANSFER, 0);
                protocol.setHeader(header);

                for (int i = 0; i < 5; i++) {
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    switch (i) {
                        case 0:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_NUMBER, 0);
                            tlv = new Protocol<>(tlvHeader, text);
                            break;
                        case 1:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ACCOUNT_HOLDER_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, account);
                            break;
                        case 2:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.BANK_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, name);
                            break;
                        case 3:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, bank);
                            break;
                        case 4:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            tlv = new Protocol<>(tlvHeader, sessionID);
                            break;


                    }

                    protocol.addChild(tlv);
                }

                try {
                    resProtocol = asyncRequest.sendAndReceive(protocol);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.CARD_MOVEMENT, 0);
                protocol.setHeader(header);

                for (int i = 0; i < 4; i++) {
                    Protocol<String> tlv = null;
                    Header tlvHeader = null;

                    switch (i) {
                        case 0:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.CARE_NUMBER, 0);
                            tlv = new Protocol<>(tlvHeader, cardNum);
                            break;
                        case 1:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.CARD_ISSUER, 0);
                            tlv = new Protocol<>(tlvHeader, cardCompany);
                            break;
                        case 2:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0);
                            tlv = new Protocol<>(tlvHeader, text);
                            break;
                        case 3:
                            tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
                            tlv = new Protocol<>(tlvHeader, sessionID);
                            break;
                    }

                    protocol.addChild(tlv);
                }
                resProtocol = PaymentController.payByCard(protocol);
            }

            if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
                System.out.println("정상적으로 납부되었습니다.");
            } else {
                System.out.println("결제가 완료되지 않았습니다. 다시 시도해주세요.");
            }
        }

    }

    public boolean paidCheck(AsyncRequest asyncRequest) {
        // 결제상태 확인하는 메서드 - 타 메서드에서 사용
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT_CHECK, 0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol<>();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
            return true;
        } else if (resProtocol.getHeader().getType() == Type.ERROR) {
            Code code = resProtocol.getHeader().getCode();
            if (code == Code.ErrorCode.INVALID_REQUEST) {
                System.out.println("미납 상태입니다.");
            } else {
                System.out.println("세션 만료. 재로그인하세요.");
            }
        }

        return false;
    }

    public void appliyTuber(AsyncRequest asyncRequest) {
        // 결핵진단서 등록
        String imagePath = "/Users/gayeong/Desktop/스크린샷 2024-12-10 오후 7.37.40.png";
        byte[] bytes;
        try {
            bytes = convertImageToByteArray(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Protocol<?> protocol = new Protocol<>();
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.UPLOAD_TUBER_REPORT, 0);
        protocol.setHeader(header);

        Header tlvHeader = new Header(Type.VALUE, DataType.RAW, Code.ValueCode.TUBER_REPORT, 0);
        Protocol<?> tlv = new Protocol<>(tlvHeader, bytes);
        protocol.addChild(tlv);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() != Code.ResponseCode.OK) {
            System.out.println("재시도하세요");
            return;
        }

        System.out.println("결핵진단서 등록이 완료되었습니다");
    }


    public byte[] convertImageToByteArray(String imagePath) throws IOException {
        //이미지를 바이트 배열로
        // FileInputStream을 사용하여 이미지 파일 읽기
        FileInputStream fis = new FileInputStream(imagePath);

        // ByteArrayOutputStream을 사용하여 바이트 배열에 저장
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 버퍼를 사용하여 데이터를 읽고 쓰기
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        // FileInputStream과 ByteArrayOutputStream을 닫기
        fis.close();
        baos.close();

        // 바이트 배열 반환
        return baos.toByteArray();
    }

    private void printSchedule(String string) {
        String[] s = string.split(" ");
        System.out.println(s[0] + " 선발 시작일: " + s[1] + " 선발 종료일: " + s[2]);
    }
}
