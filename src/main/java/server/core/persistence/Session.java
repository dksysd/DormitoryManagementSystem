package server.core.persistence;

import lombok.Getter;
import server.exception.SessionExpiredException;

import java.util.HashMap;
import java.util.Map;

/**
 * Session 클래스는 클라이언트와 관련된 세션 데이터를 관리하는 역할을 합니다.
 * 세션 ID, 생성 시간, 만료 시간, 속성 관리 등을 제공하며, 세션의 만료를 확인하고
 * 속성을 추가, 조회 또는 제거하는 기능을 지원합니다.
 */
public class Session {

    /**
     * 세션 ID를 저장
     */
    @Getter
    private final String sessionId;

    /**
     * 세션 생성 시간 (단위: 밀리초)
     */
    @Getter
    private final long creationTime;

    /**
     * 세션의 마지막 접근 시간 (단위: 밀리초)
     */
    @Getter
    private long lastAccessedTime;

    /**
     * 세션 만료 시간 (밀리초 단위)
     */
    @Getter
    private long expirationTime;

    /**
     * 세션에 저장된 속성을 관리하는 맵
     */
    private final Map<String, Object> attributes;

    /**
     * 지정된 세션 ID와 기본 만료 시간(30분)을 사용하여 새로운 세션을 생성합니다.
     *
     * @param sessionId 클라이언트 세션을 식별하기 위한 고유 ID
     */
    public Session(String sessionId) {
        this(sessionId, 30 * 60 * 1000L);
    }

    /**
     * 지정된 세션 ID와 만료 시간을 사용하여 새로운 세션을 생성합니다.
     *
     * @param sessionId      클라이언트 세션을 식별하기 위한 고유 ID
     * @param expirationTime 세션 만료 시간 (단위: 밀리초)
     */
    public Session(String sessionId, long expirationTime) {
        this.sessionId = sessionId;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.attributes = new HashMap<>();
        this.expirationTime = expirationTime;
    }

    /**
     * 세션에 새로운 속성을 추가하거나 값을 업데이트합니다.
     * 세션이 만료되었으면 예외를 발생시킵니다.
     *
     * @param key   속성의 키
     * @param value 속성의 값
     * @throws SessionExpiredException 세션이 만료된 경우
     */
    public void setAttribute(String key, Object value) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        attributes.put(key, value);
        touch();
    }

    /**
     * 세션에서 특정 속성의 값을 조회합니다.
     * 세션이 만료되었거나 타입이 맞지 않으면 예외를 발생시킵니다.
     *
     * @param key  속성의 키
     * @param type 속성의 타입(Class)
     * @param <T>  반환할 속성 타입
     * @return 속성 값
     * @throws SessionExpiredException 세션이 만료된 경우
     * @throws ClassCastException      속성의 타입이 지정한 타입과 맞지 않는 경우
     */
    public <T> T getAttribute(String key, Class<T> type) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        touch();

        Object value = attributes.get(key);

        if (value == null) return null;

        if (type.isInstance(value)) return type.cast(value);
        else throw new ClassCastException("Attribute " + key + " is not of type " + type.getName());
    }

    /**
     * 세션에서 특정 속성의 값을 조회하며, 기본값을 제공할 수 있습니다.
     * 세션이 만료되었거나 속성이 없거나 타입이 맞지 않는 경우 기본값을 반환합니다.
     *
     * @param key          속성의 키
     * @param type         속성의 타입(Class)
     * @param defaultValue 기본값
     * @param <T>          반환할 속성 타입
     * @return 속성 값 또는 기본값
     */
    public <T> T getAttribute(String key, Class<T> type, T defaultValue) {
        try {
            T value = getAttribute(key, type);
            return value == null ? defaultValue : value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    /**
     * 세션에서 특정 속성을 제거합니다.
     * 세션이 만료되었으면 예외를 발생시킵니다.
     *
     * @param key 제거할 속성의 키
     * @throws SessionExpiredException 세션이 만료된 경우
     */
    public void removeAttribute(String key) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        attributes.remove(key);
        touch();
    }

    /**
     * 세션에 지정된 키를 가진 속성 존재 여부를 확인합니다.
     * 세션이 만료되었으면 예외를 발생시킵니다.
     *
     * @param key 확인할 속성의 키
     * @return 해당 속성이 존재하면 true, 그렇지 않으면 false
     * @throws SessionExpiredException 세션이 만료된 경우
     */
    public boolean hasAttribute(String key) {
        if (isExpired()) throw new SessionExpiredException("Session expired");
        touch();
        return attributes.containsKey(key);
    }

    /**
     * 세션에서 모든 속성을 제거합니다.
     */
    public void clear() {
        attributes.clear();
    }

    /**
     * 세션의 남은 유효 시간을 반환합니다.
     *
     * @return 남은 시간(밀리초 단위) 또는 0 (만료된 경우)
     */
    public long getRemainingTime() {
        long currentTime = System.currentTimeMillis();
        long remainingTime = expirationTime - (currentTime - lastAccessedTime);
        return Math.max(0, remainingTime);
    }

    /**
     * 세션의 만료 여부를 확인합니다.
     *
     * @return 세션이 만료된 경우 true, 그렇지 않으면 false
     */
    public boolean isExpired() {
        // 현재 만료 로직은 항상 false로 설정됨 (확장 가능성 있음)
        return false;
    }

    /**
     * 세션에 접근했을 때 호출되어 마지막 접근 시간을 갱신합니다.
     */
    public void touch() {
        this.lastAccessedTime = System.currentTimeMillis();
    }
}