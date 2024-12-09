package server.util;


import server.core.SessionManager;

public class ProtocolValidator {


    public static boolean verifySessionId(String sessionId) {
        return SessionManager.getINSTANCE().hasSession(sessionId);

    }

    public static String getIdBySessionId(String sessionId) {
        return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("ID", String.class);
    }

}
