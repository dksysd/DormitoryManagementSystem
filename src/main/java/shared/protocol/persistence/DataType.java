package shared.protocol.persistence;

/**
 * DataType 열거형은 프로토콜에서 사용되는 데이터의 유형을 정의합니다.
 * 요청 또는 응답 메시지에서 데이터 해석 시 각 유형에 따라 처리 방식이 결정됩니다.
 */
public enum DataType implements HeaderElement {

    /**
     * 정수를 나타내는 데이터 유형 (예: 1, 2, 3).
     */
    INTEGER,

    /**
     * 부동소수점(float) 숫자를 나타내는 데이터 유형 (예: 1.2f, 3.14f).
     */
    FLOAT,

    /**
     * 소수점 표현을 포함한 배정밀도(double) 숫자를 나타내는 데이터 유형 (예: 1.23, 3.14159).
     */
    DOUBLE,

    /**
     * 문자열 데이터를 나타내는 데이터 유형 (예: "hello", "world").
     */
    STRING,

    /**
     * 참/거짓 데이터를 나타내는 논리 데이터 유형 (예: true, false).
     */
    BOOLEAN,

    /**
     * 원시 데이터를 나타내는 데이터 유형. 변환 없이 바이트 그대로 처리할 경우 사용.
     */
    RAW,

    /**
     * TLV(Tag-Length-Value) 형식 데이터를 나타내는 데이터 유형. 복합 데이터를 구조화하여 처리할 때 사용.
     */
    TLV
}