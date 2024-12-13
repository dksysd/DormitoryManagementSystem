package server.util;

import server.core.SessionManager;

import java.util.Objects;

/**
 * ProtocolValidator 클래스는 세션 ID를 검증하고 사용자 정보 및 권한을 확인하는 유틸리티 메서드를 제공합니다.
 * <p>
 * 이 클래스는 {@link SessionManager}와 상호작용하여 세션 및 사용자 정보에 접근합니다.
 */
public class ProtocolValidator {

    /**
     * 주어진 세션 ID가 유효한지 확인합니다.
     *
     * @param sessionId 확인할 세션 ID
     * @return 세션 ID가 유효하면 true, 그렇지 않으면 false
     */
    public static boolean verifySessionId(String sessionId) {
        return SessionManager.getINSTANCE().hasSession(sessionId);
    }

    /**
     * 주어진 세션 ID로부터 관련된 사용자의 ID를 가져옵니다.
     *
     * @param sessionId 세션 ID
     * @return 세션에 저장된 사용자 ID, 없을 경우 null
     * @throws NullPointerException 세션이 존재하지 않을 경우
     */
    public static String getIdBySessionId(String sessionId) {
        return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("ID", String.class);
    }

    /**
     * 주어진 세션 ID로부터 사용자의 유형(예: 학생, 관리자)을 가져옵니다.
     *
     * @param sessionId 세션 ID
     * @return 사용자 유형 ("student" 또는 "admin"), 없을 경우 null
     * @throws NullPointerException 세션이 존재하지 않을 경우
     */
    public static String getUserType(String sessionId) {
        return SessionManager.getINSTANCE().getSession(sessionId).getAttribute("USER_TYPE", String.class);
        // UserType은 "student"/"admin"으로 나뉩니다.
    }

    /**
     * 사용자가 학생인지 여부를 확인합니다.
     *
     * @param sessionId 세션 ID
     * @return 사용자가 학생("student")이면 true, 그렇지 않으면 false
     */
    public static boolean isStudent(String sessionId) {
        return Objects.equals(getUserType(sessionId), "student");
    }

    /**
     * 사용자가 관리자인지 여부를 확인합니다.
     *
     * @param sessionId 세션 ID
     * @return 사용자가 관리자("admin")이면 true, 그렇지 않으면 false
     */
    public static boolean isAdmin(String sessionId) {
        return Objects.equals(getUserType(sessionId), "admin");
    }
}