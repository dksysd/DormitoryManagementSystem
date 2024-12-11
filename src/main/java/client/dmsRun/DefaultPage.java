package client.dmsRun;

import client.auth.Auth;
import client.core.util.AsyncRequest;
import client.domitoryAdmin.AdminPage;
import client.domitoryUser.ApplicantPage;

import java.util.concurrent.ExecutionException;

public class DefaultPage {
    public static void run(String host, int port) throws ExecutionException, InterruptedException {
        AsyncRequest asyncRequest;
        try {
            asyncRequest = new AsyncRequest(host,port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //<로그인 화면>
        //관리자 = 0 , 학생 = 1, 로그인 실패 = -1
        int identity = 2;
        do{
            Auth auth = new Auth();
            identity = auth.logIn(asyncRequest);
            //<로그인 후 선택하는 화면>
            if(identity == 0){
                //관리자 화면
                new AdminPage(auth.getSessionID()).adminFunction(asyncRequest);
                auth.logOut(asyncRequest);
            }
            else if(identity == 1){
                //학생 화면
                new ApplicantPage(auth.getSessionID()).applicantFunction(asyncRequest);
                auth.logOut(asyncRequest);
            }
            else {
                System.out.println("=============로그인 실패!=============");
                return;
            }
        } while (identity == -1);
    }
}
