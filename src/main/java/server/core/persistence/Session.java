package server.core.persistence;

import lombok.Getter;
import server.exception.SessionExpiredException;

import java.util.HashMap;
import java.util.Map;

public class Session {
    @Getter
    private final String sessionId;
    @Getter
    private final long creationTime;
    private final Map<String, Object> attributes;

    @Getter
    private long lastAccessedTime;
    @Getter
    private long expirationTime;

    public Session(String sessionId) {
        this(sessionId, 30 * 60 * 1000L);
    }

    public Session(String sessionId, long expirationTime) {
        this.sessionId = sessionId;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.attributes = new HashMap<>();
        this.expirationTime = expirationTime;
    }

    public void setAttribute(String key, Object value) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        attributes.put(key, value);
        touch();
    }


    public <T> T getAttribute(String key, Class<T> type) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        touch();

        Object value = attributes.get(key);

        if (value == null) return null;

        if (type.isInstance(value)) return type.cast(value);
        else throw new ClassCastException("Attribute " + key + " is not of type " + type.getName());
    }

    public <T> T getAttribute(String key, Class<T> type, T defaultValue) {
        try {
            T value = getAttribute(key, type);
            return value == null ? defaultValue : value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void removeAttribute(String key) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        attributes.remove(key);
        touch();
    }

    public boolean hasAttribute(String key) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        touch();
        return attributes.containsKey(key);
    }

    public void clear() {
        attributes.clear();
    }

    public long getRemainingTime() {
        long currentTime = System.currentTimeMillis();
        long remainingTime = expirationTime - (currentTime - lastAccessedTime);
        return Math.max(0, remainingTime);
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastAccessedTime) > expirationTime;
    }

    private void touch() {
        this.lastAccessedTime = System.currentTimeMillis();
    }
}
