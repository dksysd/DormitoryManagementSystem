package client.auth;

import lombok.Getter;
import server.controller.AuthController;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Auth {
    private String id;
    private String pw;
    @Getter
    private String sessionID;

    public int logIn(){
        logInInfo();
        return logInCheck(id, pw);
    }

    public void logInInfo(){
        Scanner sc = new Scanner(System.in);

        System.out.println("=============생활관 관리 시스템==============");
        System.out.println();
        System.out.print("학번 : ");
        id = sc.next();
        System.out.print("PW : ");
        pw = sc.next();
    }

    public int logInCheck(String id, String pw){
        //관리자 = 0 , 학생 = 1, 로그인 실패 = -1
        Protocol<?> protocol = new Protocol<>();
        Header header = new Header(Type.REQUEST, DataType.TLV, Code.RequestCode.LOGIN,0);
        protocol.setHeader(header);

        Header tlvHeader = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ID, 0);
        Protocol<String> tlv = new Protocol<>(tlvHeader, id);
        protocol.addChild(tlv);

        Header tlvHeader2 = new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PASSWORD, 0);
        Protocol<String> tlv2 = new Protocol<>(tlvHeader, pw);
        protocol.addChild(tlv2);

        Protocol<?> resProtocol = null;
        try {
            resProtocol = AuthController.login(protocol);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sessionID = (String) resProtocol.getChildren().get(0).getData();;
        String type = (String) resProtocol.getChildren().get(1).getData();;

        if(type.equals("관리자"))
            return 0;
        else if (type.equals("학생"))
            return 1;
        else return -1;
    }
}
