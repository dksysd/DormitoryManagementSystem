package server.exception;

/**
 * IllegalHeaderElementException 클래스는 잘못된 헤더 요소가 발견되었을 때 발생하는 사용자 정의 런타임 예외입니다.
 * 이 클래스는 {@link RuntimeException}을 상속하며, 헤더 구성 요소와 관련된 문제를 처리할 때 사용됩니다.
 */
public class IllegalHeaderElementException extends RuntimeException {

    /**
     * IllegalHeaderElementException 생성자.
     * 잘못된 헤더 요소와 관련된 사용자 정의 메시지를 포함하여 예외를 생성합니다.
     *
     * @param message 예외와 관련된 상세 메시지
     */
    public IllegalHeaderElementException(String message) {
        super(message);
    }
}