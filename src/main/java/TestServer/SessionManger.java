package TestServer;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManger {
    @Getter
    private static final SessionManger instance = new SessionManger();

    private final Map<String, Session> sessions;
    private final ScheduledExecutorService scheduler;
    private final long defaultSessionTimeout;

    private SessionManger() {
        sessions = new ConcurrentHashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        defaultSessionTimeout = 30 * 60 * 1000;

        scheduler.scheduleAtFixedRate(this::clearExpiredSessions, 1, 1, TimeUnit.SECONDS);
    }

    private void clearExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> now - entry.getValue().getLastAccessTime() > entry.getValue().getTimeout());
    }

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public String createSession() {
        return createSession(defaultSessionTimeout);
    }

    public String createSession(long timeout) {
        Session session = new Session(timeout);
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, session);
        return sessionId;
    }

    public void destroySession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
