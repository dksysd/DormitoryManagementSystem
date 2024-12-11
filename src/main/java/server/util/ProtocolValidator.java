package server.util;


import server.core.SessionManager;

import java.util.Objects;

public class ProtocolValidator {


    public static boolean verifySessionId(String sessionId) {
        return SessionManager.getINSTANCE().hasSession(sessionId);

    }

    public static String getIdBySessionId(String sessionId) {
        return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("ID", String.class);
    }
    public static int getUserType(String sessionId) {
       return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("USER_TYPE", Integer.class);
        //UserType은 Student/Admin으로 나뉨
    }
    public static boolean isStudent(String sessionId) {
        System.out.println(getUserType(sessionId));
       return getUserType(sessionId)== 1;
    }
    public static boolean isAdmin(String sessionId) {
        System.out.println(getUserType(sessionId));
        return Objects.equals(getUserType(sessionId), 2);
    }
}
