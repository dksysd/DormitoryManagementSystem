package server.exception;

/**
 * SessionExpiredException 클래스는 세션이 만료되었을 때 발생하는 사용자 정의 런타임 예외입니다.
 * 이 클래스는 {@link RuntimeException}을 상속하며, 세션 관리 중 발생하는 만료 문제를 처리하는 데 사용됩니다.
 */
public class SessionExpiredException extends RuntimeException {

    /**
     * SessionExpiredException 생성자.
     * 만료된 세션과 관련된 사용자 정의 메시지를 포함하여 예외를 생성합니다.
     *
     * @param message 예외와 관련된 상세 메시지
     */
    public SessionExpiredException(String message) {
        super(message);
    }
}