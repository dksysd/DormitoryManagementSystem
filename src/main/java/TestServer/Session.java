package TestServer;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private final Map<String, Object> attributes;
    @Getter
    private long lastAccessTime;
    @Getter
    private final long timeout;

    public Session(long timeout) {
        attributes = new ConcurrentHashMap<>();
        lastAccessTime = System.currentTimeMillis();
        this.timeout = timeout;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void touch() {
        lastAccessTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Session{" +
                "attributes=" + attributes +
                ", lastAccessTime=" + lastAccessTime +
                ", timeout=" + timeout +
                '}';
    }
}
