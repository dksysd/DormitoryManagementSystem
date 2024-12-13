package shared.protocol.persistence;

import server.exception.IllegalHeaderElementException;

/**
 * HeaderElement 인터페이스는 프로토콜 헤더에서 요소로 사용될 수 있는 객체의 동작을 정의합니다.
 * Enum을 기반으로 하여 바이트 변환과 역변환 기능을 제공하며, 헤더 요소로 동작하는 구조체의 규약을 정의합니다.
 */
public interface HeaderElement {

    /**
     * 현재 HeaderElement를 바이트(byte)로 변환합니다.
     * Enum 객체를 기반으로 `ordinal()` 값을 바이트로 변환하며,
     * 값이 바이트 범위를 초과하거나 Enum이 아닌 경우 예외를 발생시킵니다.
     *
     * @return 변환된 바이트 값
     * @throws IllegalHeaderElementException HeaderElement가 Enum 객체로 구현되지 않았거나, 값이 바이트 범위를 초과할 경우 발생
     */
    default byte toByte() {
        if (this instanceof Enum<?> e) {
            int ordinal = e.ordinal();
            if (ordinal > Byte.MAX_VALUE) {
                throw new IllegalHeaderElementException("The value " + ordinal + " is too long. [" + e + "]");
            }
            return (byte) ordinal;
        } else {
            throw new IllegalHeaderElementException("HeaderElement must be implemented by an Enum.");
        }
    }

    /**
     * 주어진 바이트 값과 Enum 타입을 기반으로 헤더 요소 객체를 반환합니다.
     * 바이트 값은 Enum의 상수 목록 배열에서 해당 순서(ordinal)를 참조하여 Enum 객체로 변환됩니다.
     *
     * @param b 변환할 바이트 값
     * @param type 반환할 Enum 객체의 클래스 타입
     * @param <T> HeaderElement를 구현한 Enum 객체의 타입
     * @return 변환된 Enum 객체
     * @throws ArrayIndexOutOfBoundsException 바이트 값이 Enum의 상수에 해당하지 않을 경우 발생
     */
    static <T extends Enum<T> & HeaderElement> T toHeaderElement(byte b, Class<T> type) {
        return type.getEnumConstants()[b];
    }
}
