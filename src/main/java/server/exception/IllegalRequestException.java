package server.exception;

/**
 * IllegalRequestException 클래스는 잘못된 요청이 발생했을 때 던지는 사용자 정의 런타임 예외입니다.
 * 이 클래스는 {@link RuntimeException}을 상속하며, 요청의 유효성 검사를 실행할 때 발생할 수 있는
 * 문제를 처리하는 데 사용됩니다.
 */
public class IllegalRequestException extends RuntimeException {

    /**
     * IllegalRequestException 생성자.
     * 잘못된 요청과 관련된 사용자 정의 메시지를 포함하여 예외를 생성합니다.
     *
     * @param message 예외와 관련된 상세 메시지
     */
    public IllegalRequestException(String message) {
        super(message);
    }
}