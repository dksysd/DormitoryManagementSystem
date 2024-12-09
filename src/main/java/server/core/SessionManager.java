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

public class SessionManager {
    @Getter
    private static final SessionManager INSTANCE = new SessionManager();

    private final Map<String, Session> sessions;
    private final ScheduledExecutorService scheduler;
    private final int DEFAULT_SESSION_EXPIRATION_TIME;

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

    public Session createSession() {
        return createSession(DEFAULT_SESSION_EXPIRATION_TIME);
    }

    public Session createSession(long expirationTime) {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, expirationTime);
        sessions.put(sessionId, session);
        return session;
    }

    public Session getSession(String sessionId) {
        Session session = sessions.get(sessionId);

        if (session == null || session.isExpired()) {
            sessions.remove(sessionId);
            return null;
        }

        return session;
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    private void clearExpiredSessions() {
        sessions.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private void shutdown() {
        sessions.clear();
        scheduler.shutdown();
    }

    public boolean hasSession(String sessionId) {
        Session session = sessions.get(sessionId);
        return session != null && !session.isExpired();
    }
}
