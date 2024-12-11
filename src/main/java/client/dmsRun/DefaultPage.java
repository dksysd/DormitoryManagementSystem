package client.dmsRun;

import client.auth.Auth;
import client.core.util.AsyncRequest;
import client.domitoryAdmin.AdminPage;
import client.domitoryUser.ApplicantPage;

import java.util.concurrent.ExecutionException;

public class DefaultPage {
    public static void run(){
        AsyncRequest asyncRequest;
        try {
            asyncRequest = new AsyncRequest("localhost",4_000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //<로그인 화면>
        //관리자 = 0 , 학생 = 1, 로그인 실패 = -1
        Auth auth = new Auth();
        int identity = auth.logIn(asyncRequest);

        //<로그인 후 선택하는 화면>
        if(identity == 0){
            //관리자 화면
            new AdminPage(auth.getSessionID()).adminFunction(asyncRequest);
        }
        else if(identity == 1){
            //학생 화면
            new ApplicantPage(auth.getSessionID()).applicantFunction(asyncRequest);
        }
        else {
            System.out.println("=============로그인 실패!=============");
        }
    }
}
