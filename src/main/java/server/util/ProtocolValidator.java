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
    public static String getUserType(String sessionId) {
       return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("UserType", String.class);
        //UserType은 Student/Admin으로 나뉨
    }
    public static boolean isStudent(String sessionId) {
       return Objects.equals(getIdBySessionId(sessionId), "Student");
    }
    public static boolean isAdmin(String sessionId) {
        return Objects.equals(getIdBySessionId(sessionId), "Admin");
    }
}
