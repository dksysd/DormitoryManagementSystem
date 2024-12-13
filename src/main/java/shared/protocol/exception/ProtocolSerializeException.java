package shared.protocol.exception;

public class ProtocolSerializeException extends RuntimeException {
    /**
     * ProtocolSerializeException 클래스의 생성자.
     * 주어진 상세 메시지와 함께 새로운 ProtocolSerializeException 인스턴스를 생성합니다.
     *
     * @param message 예외의 원인에 대한 설명을 포함하는 상세 메시지
     */
    public ProtocolSerializeException(String message) {
        super(message);
    }
}
