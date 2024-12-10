package client.domitoryAdmin;

import shared.protocol.persistence.*;

import java.util.Scanner;

public class AdminPage {
    private static Scanner sc = new Scanner(System.in);
    private String sessionID;

    public AdminPage(){}
    public AdminPage(String sessionID){
        this.sessionID = sessionID;
    }

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
                default: break;
            }
        } while (option != 8);
    }

    public static void adminFunctionInfo(){
        System.out.println("============= 관리자 페이지입니다 =============");
        System.out.println("1. 선발 일정 등록"); // 다듬기
        System.out.println("2. 입사 신청자 목록 확인");
        System.out.println("3. 입사자 선발하기"); // 다듬기
        System.out.println("4. 상벌점 관리"); // 다듬기
        System.out.println("5. 결핵진단서 진위 확인");
        System.out.println("6. 퇴사 승인");
        System.out.println("7. 우선선발 증빙서 진위 확인");
        System.out.println("8. 로그아웃");
        System.out.println();
        System.out.println();
    }

    public void registerSelectionInfo(){

        int selection = 0;
        for( ; ; ){
            System.out.println("어떤 선발의 일정을 등록하시겠습니까?");
            System.out.print("(1. 우선선발 일정 등록 / 2. 일반선발 일정 등록 / 3. 추가선발 일정등록 / 4. 등록종료) : ");
            selection = sc.nextInt();

            if(selection > 3 || selection < 1){
                break;
            }

            System.out.println("선발 시작일을 입력하세요.");
            System.out.println("ex : yyyy년 mm월 dd일부터 - (yyyymmdd)");
            String day = sc.next();

            Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.REGISTER_SELECTION_INFO,0);
            Protocol<?> protocol = new Protocol();
            protocol.setHeader(header);

            Header tlvHeader, tlvHeader2;
            Protocol<String> tlv, tlv2;
            //각 선발별로 메시지 보내기
            switch (selection){
                case 1 :
                    tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_INFO, 0);
                    tlv = new Protocol<>(tlvHeader, "우선선발");
                    protocol.addChild(tlv);
                    tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_SCHEDULE, 0);
                    tlv2 = new Protocol<>(tlvHeader2, day);
                    protocol.addChild(tlv2);
                    break;
                case 2 :
                    tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_INFO, 0);
                    tlv = new Protocol<>(tlvHeader, "일반선발");
                    protocol.addChild(tlv);
                    tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_SCHEDULE, 0);
                    tlv2 = new Protocol<>(tlvHeader2, day);
                    protocol.addChild(tlv2);
                    break;
                case 3 :
                    tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_INFO, 0);
                    tlv = new Protocol<>(tlvHeader, "추가선발");
                    protocol.addChild(tlv);
                    tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SELECTION_SCHEDULE, 0);
                    tlv2 = new Protocol<>(tlvHeader2, day);
                    protocol.addChild(tlv2);
                    break;
            }

            Protocol<?> resProtocol = null;

            //resProtocol = DomitoryAdminController.(protocol);

            if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
                System.out.println("일정등록이 완료되었습니다");
            } else {
                System.out.println("재시도해주세요.");
            }

        }

    }

    public void displayApplicants(){
        // 반복문으로 메시지 계속 받을 거 가틍데요 ~ 사용자 입력이 없그든여
        // 메시지 안에 내용 가꼬 와서 출력 형식만 다듬으면 됨
    }

    public void selectApplicant(){
        System.out.print("선발/배정을 시작하시겠습니까? Y/N");
        char selection = sc.next().charAt(0);
        if(selection != 'Y' && selection != 'y'){
            return;
        }

        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.GET_SELECTION_SCHEDULE,0);
        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, sessionID);
        Protocol<?> protocol = new Protocol();
        protocol.setHeader(header);
        protocol.addChild(tlv);

        Protocol<?> resProtocol = null;

        //resProtocol = DomitoryAdminController.(protocol);

        if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
            System.out.println("선발/배정이 완료되었습니다");
        } else {
            System.out.println("재시도해주세요.");
        }
    }

    public void ManagementMeritPoint(){
        System.out.print("관리하려는 학생의 학번을 입력하세요 : ");
        String student = sc.next();
        System.out.println("상점/벌점 입력 (벌점의 경우 음수로 입력하세요) : ");
        int point = sc.nextInt();

        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.MANAGEMENT_MERIT_POINT,0);
        Protocol<?> protocol = new Protocol();
        protocol.setHeader(header);

        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, student);
        protocol.addChild(tlv);

        Header tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SUM_OF_MERIT_POINTS, 0);
        Protocol<Integer> tlv2 = new Protocol<>(tlvHeader2, point);
        protocol.addChild(tlv2);

        Header tlvHeader3 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.SESSION_ID, 0);
        Protocol<String> tlv3 = new Protocol<>(tlvHeader3, sessionID);
        protocol.addChild(tlv3);

        Protocol<?> resProtocol = null;

        //resProtocol = DomitoryAdminController.(protocol);

        if(resProtocol.getHeader().getCode() == Code.ResponseCode.OK){
            System.out.println("상벌점 입력이 정상적으로 완료되었습니다");
        } else {
            System.out.println("재시도해주세요.");
        }

    }

    public void ConfirmTuberReport(){
        // 결핵진단서 확인 안한 사람들 불러오는 쿼리있는 메서드
        // 한놈씩 반복문으로 조져서 그놈들이 올린 결핵진단서 메시지로 받아서 불러오는 메서드
        System.out.println("1.승인 / 2.거절 (번호를 누르세요) : ");
        // 승인거절 결과 보내는 메서드
        System.out.println("다음 학생의 결핵진단서를 불러올까요? (1. 승인 / 2. 거절) : ");
        // 값에따라 반복문 계속할지?
    }

    public void DisplayMoveOutApplicant(){
        //메시지 보내서 퇴사신청한놈들 불러오는 메서드
        // 그놈들 환불상태 불러오는 메서드
        // 그럼 그거 보고 퇴사승인 누르기
        // 승인하면 또 메시지 보내서 학생들 상태 바꾸기
    }

    public void ConfirmFIleForProof(){
        //위에 결핵이랑 똑같이
    }
}
