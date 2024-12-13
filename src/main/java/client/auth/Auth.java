package client.auth;

import client.core.util.AsyncRequest;
import lombok.Getter;
import server.controller.AuthController;
import shared.protocol.persistence.*;

import java.util.Objects;
import java.util.Scanner;

/**
 * Auth 클래스는 인증 및 로그인 관리 시스템을 담당합니다.
 * <p>
 * 로그인을 통해 사용자 세션을 생성하고, 세션을 기반으로 요청을 처리하거나 로그아웃 기능을 수행합니다.
 */
public class Auth {

    /**
     * 사용자 ID (학번 등)
     */
    private String id;

    /**
     * 사용자 비밀번호
     */
    private String pw;

    /**
     * 인증된 세션 ID
     */
    @Getter
    private String sessionID;

    /**
     * 사용자 로그인 프로세스를 처리합니다.
     * <p>
     * 사용자 입력을 통해 로그인 정보를 가져오고, 이를 기반으로 서버와 검증을 수행합니다.
     *
     * @param asyncRequest 비동기 요청 객체
     * @return 로그인 성공 시 사용자 유형 (0 = 관리자, 1 = 학생), 실패 시 -1 반환
     */
    public int logIn(AsyncRequest asyncRequest) {
        logInInfo(); // 사용자 입력 정보 요청
        return logInCheck(asyncRequest, id, pw); // 서버와 연동하여 로그인 검증
    }

    /**
     * 사용자로부터 학번과 비밀번호를 입력받습니다.
     */
    public void logInInfo() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=============생활관 관리 시스템==============");
        System.out.println();
        System.out.print("학번 : ");
        id = sc.next();
        System.out.print("PW : ");
        pw = sc.next();
    }

    /**
     * 서버와 통신하여 로그인 정보를 검증합니다.
     * <p>
     * 서버에 요청 프로토콜을 보내고, 세션 ID를 생성하거나 오류를 반환합니다.
     *
     * @param asyncRequest 비동기 요청 객체
     * @param id           사용자 ID
     * @param pw           사용자 비밀번호
     * @return 로그인 성공 시 사용자 유형 (0 = 관리자, 1 = 학생), 실패 시 -1 반환
     */
    public int logInCheck(AsyncRequest asyncRequest, String id, String pw) {
        // 관리자 = 0, 학생 = 1, 로그인 실패 = -1
        Protocol<?> protocol = new Protocol<>();
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.LOGIN, 0);
        protocol.setHeader(header);

        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, id);
        protocol.addChild(tlv);

        Header tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PASSWORD, 0);
        Protocol<String> tlv2 = new Protocol<>(tlvHeader2, pw);
        protocol.addChild(tlv2);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e); // 오류 발생 시 예외 처리
        }

        if (resProtocol.getHeader().getType() == Type.ERROR) {
            System.out.println("로그인 오류");
            return -1;
        } else {
            sessionID = (String) resProtocol.getChildren().getLast().getData(); // 세션 ID 설정

            String type = (String) resProtocol.getChildren().getFirst().getData();
            if (Objects.equals(type, "student"))
                return 1; // 학생 사용자
            else if (Objects.equals(type, "admin"))
                return 0; // 관리자 사용자
            else
                return -1; // 알 수 없는 사용자 유형
        }
    }

    /**
     * 현재 인증된 세션을 로그아웃합니다.
     * <p>
     * 서버와 통신하여 세션을 종료하고 로그아웃 상태를 반환합니다.
     *
     * @param asyncRequest 비동기 요청 객체
     */
    public void logOut(AsyncRequest asyncRequest) {
        Protocol<?> protocol = new Protocol<>();
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.LOGOUT, 0);
        protocol.setHeader(header);

        Protocol<?> resProtocol;
        try {
            resProtocol = asyncRequest.sendAndReceive(protocol);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (resProtocol.getHeader().getCode() == Code.ResponseCode.OK) {
            System.out.println("로그아웃 합니다."); // 로그아웃 성공 메시지 출력
        }
    }
}