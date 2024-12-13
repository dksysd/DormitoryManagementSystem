package server.core;

import lombok.Getter;
import server.config.Config;
import server.core.persistence.Session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SessionManager 클래스는 세션을 생성, 관리, 제거하는 역할을 수행합니다.
 * 세션 관리를 효율적으로 수행하기 위해 스레드-안전한 컬렉션과 예약된 작업 스케줄러를 사용하여
 * 만료된 세션을 주기적으로 정리합니다.
 */
public class SessionManager {

    /**
     * SessionManager 싱글톤 인스턴스
     */
    @Getter
    private static final SessionManager INSTANCE = new SessionManager();

    /**
     * 세션을 저장하는 스레드-안전한 맵
     */
    private final Map<String, Session> sessions;

    /**
     * 주기적으로 만료된 세션을 제거하는 스케줄러
     */
    private final ScheduledExecutorService scheduler;

    /**
     * 기본 세션 만료 시간 (밀리초 단위)
     */
    private final int DEFAULT_SESSION_EXPIRATION_TIME;

    /**
     * SessionManager의 생성자.
     * 세션 맵과 스케줄러를 초기화하며, 주기적으로 만료된 세션을 정리하는 작업을 예약합니다.
     */
    private SessionManager() {
        sessions = new ConcurrentHashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();

        DEFAULT_SESSION_EXPIRATION_TIME = Config.getInt("DEFAULT_SESSION_EXPIRATION_TIME");
        int DEFAULT_CLEANUP_INTERVAL = Config.getInt("DEFAULT_CLEANUP_INTERVAL");

        scheduler.scheduleAtFixedRate(this::clearExpiredSessions,
                DEFAULT_CLEANUP_INTERVAL,
                DEFAULT_CLEANUP_INTERVAL,
                TimeUnit.MILLISECONDS);
    }

    /**
     * 새 세션을 생성하고 기본 만료 시간을 설정합니다.
     *
     * @return 새로 생성된 세션
     */
    public Session createSession() {
        return createSession(DEFAULT_SESSION_EXPIRATION_TIME);
    }

    /**
     * 지정된 만료 시간을 가지는 새 세션을 생성합니다.
     *
     * @param expirationTime 새 세션의 만료 시간 (밀리초 단위)
     * @return 새로 생성된 세션
     */
    public Session createSession(long expirationTime) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, expirationTime);
        sessions.put(sessionId, session);
        return session;
    }

    /**
     * 세션 ID를 사용하여 세션을 검색합니다.
     * 세션이 존재하지 않거나 만료된 경우 null을 반환합니다.
     *
     * @param sessionId 검색할 세션의 ID
     * @return 검색된 세션 또는 null
     */
    public Session getSession(String sessionId) {
        Session session = sessions.get(sessionId);

        if (session == null || session.isExpired()) {
            sessions.remove(sessionId);
            return null;
        }

        return session;
    }

    /**
     * 특정 세션을 삭제합니다.
     *
     * @param sessionId 삭제할 세션의 ID
     */
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * 만료된 세션을 맵에서 제거합니다.
     * 이 메서드는 스케줄러에 의해 주기적으로 호출됩니다.
     */
    private void clearExpiredSessions() {
        sessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    /**
     * SessionManager를 종료합니다.
     * 모든 세션을 제거하고 스케줄러를 종료합니다.
     */
    private void shutdown() {
        sessions.clear();
        scheduler.shutdown();
    }

    /**
     * 특정 세션 ID를 가진 세션이 존재하며 유효한지 확인합니다.
     *
     * @param sessionId 확인할 세션의 ID
     * @return 세션이 존재하고 만료되지 않았다면 true, 그렇지 않으면 false
     */
    public boolean hasSession(String sessionId) {
        Session session = sessions.get(sessionId);
        return session != null && !session.isExpired();
    }
}