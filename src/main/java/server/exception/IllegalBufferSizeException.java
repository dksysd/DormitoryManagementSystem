package server.exception;

/**
 * IllegalBufferSizeException 클래스는 버퍼 크기가 유효하지 않을 때 발생하는 사용자 정의 예외입니다.
 * 이 예외는 {@link RuntimeException}을 상속하며, 실행 시점에서 잘못된 버퍼 크기와 관련된 에러를 제공합니다.
 */
public class IllegalBufferSizeException extends RuntimeException {

    /**
     * IllegalBufferSizeException 생성자.
     * 잘못된 버퍼 크기와 관련된 사용자 정의 메시지를 포함하여 예외를 생성합니다.
     *
     * @param message 예외와 관련된 상세 메시지
     */
    public IllegalBufferSizeException(String message) {
        super(message);
    }
}