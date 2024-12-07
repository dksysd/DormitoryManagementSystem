package client.dmsRun;

import client.auth.LogInOut;

public class DefaultPage {
    public static void run(){
        //<로그인 화면>
        LogInOut logInOut = new LogInOut();
        logInOut.logInInfo();
        // >>  메시지 처리하는 메서드 >> 실제 저장된 아이디 비번 뽑아냄
        int identity = logInOut.logInCheck("20230377","20230377");
        // 윗줄을 int 리턴 타입의 함수로 한번에 묶어버릴까? loginout 클래스의 함수로 묶어두기?

        //<로그인 후 선택하는 화면>
        if(identity == 0){
            //관리자 화면
        }
        else if(identity == 1){
            //학생 화면
        }
        else {
            System.out.println("=============로그인 실패!=============");
        }
    }
}
