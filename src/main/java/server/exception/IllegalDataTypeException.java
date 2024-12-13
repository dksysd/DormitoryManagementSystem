package server.exception;

/**
 * IllegalDataTypeException 클래스는 잘못된 데이터 타입이 사용되었을 때 발생하는 사용자 정의 예외입니다.
 * 이 예외는 {@link RuntimeException}을 상속하며, 실행 시점에서 적절하지 않은 데이터 타입과 관련된 에러를 제공합니다.
 */
public class IllegalDataTypeException extends RuntimeException {

    /**
     * IllegalDataTypeException 생성자.
     * 잘못된 데이터 타입과 관련된 사용자 정의 메시지를 포함하여 예외를 생성합니다.
     *
     * @param message 예외와 관련된 상세 메시지
     */
    public IllegalDataTypeException(String message) {
        super(message);
    }
}