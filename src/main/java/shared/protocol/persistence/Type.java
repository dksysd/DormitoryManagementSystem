package shared.protocol.persistence;

/**
 * Type 열거형은 프로토콜 메시지의 유형을 정의합니다.
 * 각 값은 프로토콜 메시지가 표현하는 목적이나 상태를 나타냅니다.
 */
public enum Type implements HeaderElement {

    /**
     * 요청(Request)을 나타내는 유형.
     * 클라이언트가 서버로 작업을 요청하는 메시지를 표현합니다.
     */
    REQUEST,

    /**
     * 응답(Response)을 나타내는 유형.
     * 서버가 클라이언트의 요청에 대한 결과를 반환하는 메시지를 표현합니다.
     */
    RESPONSE,

    /**
     * 오류(Error)을 나타내는 유형.
     * 요청이나 처리에서 발생한 문제가 포함된 메시지를 표현합니다.
     */
    ERROR,

    /**
     * 데이터 값(Value)을 나타내는 유형.
     * 특정한 데이터를 전달하기 위한 메시지를 표현합니다.
     */
    VALUE
}