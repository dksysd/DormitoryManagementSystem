package client.dmsRun;

import client.auth.Auth;
import client.domitoryAdmin.AdminPage;
import client.domitoryUser.ApplicantPage;

public class DefaultPage {
    public static void run(){
        //<로그인 화면>
        //관리자 = 0 , 학생 = 1, 로그인 실패 = -1
        Auth auth = new Auth();
        int identity = auth.logIn();

        //<로그인 후 선택하는 화면>
        if(identity == 0){
            //관리자 화면
            new AdminPage(auth.getSessionID()).adminFunction();
        }
        else if(identity == 1){
            //학생 화면
            new ApplicantPage(auth.getSessionID()).applicantFunction();
        }
        else {
            System.out.println("=============로그인 실패!=============");
        }
    }
}
