package shared.protocol.persistence;

import lombok.*;

import java.util.Objects;

/**
 * Header 클래스는 프로토콜 메시지의 헤더 정보를 캡슐화한 데이터 구조입니다.
 * 메시지의 유형, 데이터 유형, 코드 및 데이터 길이를 저장하며,
 * 통신 프로토콜에서 유효성 검증 및 데이터 처리에 사용됩니다.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Header {

    /**
     * 헤더의 고정 바이트 크기.
     */
    public static final byte BYTES = 7;

    /**
     * 메시지의 유형을 지정하는 필드.
     */
    private Type type;

    /**
     * 데이터의 형식을 정의하는 필드.
     */
    private DataType dataType;

    /**
     * 처리 또는 상태를 나타내는 코드.
     */
    private Code code;

    /**
     * 실제 데이터의 길이를 나타내는 필드.
     */
    private int dataLength;

    /**
     * 객체가 지정된 헤더와 동일한지 비교합니다.
     *
     * @param o 비교할 객체
     * @return 같으면 true, 다르면 false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Header header)) return false;
        return dataLength == header.dataLength
                && type == header.type
                && dataType == header.dataType
                && Objects.equals(code, header.code);
    }

    /**
     * 현재 헤더 객체의 해시 코드를 생성합니다.
     *
     * @return 해시 코드 값
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, dataType, code, dataLength);
    }

    /**
     * 현재 헤더 객체를 문자열로 표현합니다.
     *
     * @return 헤더 정보의 문자열 표현
     */
    @Override
    public String toString() {
        return "Header{" +
                "type=" + type +
                ", dataType=" + dataType +
                ", code=" + code +
                ", dataLength=" + dataLength +
                '}';
    }
}
