package shared.protocol.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Protocol 클래스는 특정 데이터와 프로토콜 헤더를 포함한 계층적 메시지 구조를 표현합니다.
 * 루트(Parent) 프로토콜과 하위(Child) 프로토콜 간 관계를 관리하며,
 * 프로토콜 메시지에 관련된 데이터 구조를 캡슐화하여 제공합니다.
 *
 * @param <T> 데이터의 타입 (프로토콜에 포함된 데이터의 유형을 나타냄)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Protocol<T> {

    /**
     * 프로토콜의 헤더로, 메시지 메타데이터를 포함합니다.
     */
    private Header header;

    /**
     * 프로토콜에 포함된 데이터.
     */
    private T data;

    /**
     * 하위 프로토콜을 포함하는 리스트.
     */
    private final List<Protocol<?>> children = new ArrayList<>();

    /**
     * 새로운 하위 프로토콜을 추가합니다.
     *
     * @param child 추가할 하위 프로토콜
     * @return 추가된 하위 프로토콜 객체
     */
    public Protocol<?> addChild(Protocol<?> child) {
        children.add(child);
        return child;
    }

    /**
     * 특정 하위 프로토콜을 제거합니다.
     *
     * @param child 제거할 하위 프로토콜
     */
    public void removeChild(Protocol<?> child) {
        children.remove(child);
    }

    /**
     * 현재 Protocol 객체가 주어진 객체와 동일한지를 비교합니다.
     *
     * @param o 비교할 객체
     * @return 같으면 true, 다르면 false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Protocol<?> protocol)) return false;
        return Objects.equals(header, protocol.header)
                && Objects.equals(data, protocol.data)
                && Objects.equals(children, protocol.children);
    }

    /**
     * Protocol 객체의 고유 해시 코드를 반환합니다.
     *
     * @return Protocol 객체의 해시 코드
     */
    @Override
    public int hashCode() {
        return Objects.hash(header, data, children);
    }

    /**
     * Protocol 객체를 문자열로 표현합니다.
     *
     * @return Protocol 객체의 문자열 표현
     */
    @Override
    public String toString() {
        return "Protocol{" +
                "header=" + header +
                ", data=" + data +
                ", children=" + children +
                '}';
    }
}
