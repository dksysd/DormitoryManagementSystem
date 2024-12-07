package client.auth;

import java.util.Scanner;

public class LogInOut {
    private String id;
    private String pw;

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
