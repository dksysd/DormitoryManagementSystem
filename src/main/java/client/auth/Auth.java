package client.auth;

import java.util.Scanner;

public class Auth {
    private String id;
    private String pw;

    public int logIn(){
        logInInfo();
        // >>  메시지 처리하는 메서드 >> 실제 저장된 아이디 비번 뽑아냄 .. > 로그인체크 메서드 매개변수 달라질수도
        return logInCheck("20230377","20230377");
    }

    public void logInInfo(){
        Scanner sc = new Scanner(System.in);

        System.out.println("=============생활관 관리 시스템==============");
        System.out.println();
        System.out.print("ID : ");
        id = sc.next();
        System.out.print("PW : ");
        pw = sc.next();
    }

    public int logInCheck(String id, String pw){
        //관리자 = 0 , 학생 = 1, 로그인 실패 = -1

        return -1;
    }
}
