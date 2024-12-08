package shared.protocol.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Protocol<T> {
    private Header header;
    private T data;
    private final List<Protocol<?>> children = new ArrayList<>();

    public Protocol<?> addChild(Protocol<?> child) {
        children.add(child);
        return child;
    }

    public void removeChild(Protocol<?> child) {
        children.remove(child);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Protocol<?> protocol)) return false;
        return Objects.equals(header, protocol.header) && Objects.equals(data, protocol.data) && Objects.equals(children, protocol.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, data, children);
    }

    @Override
    public String toString() {
        return "Protocol{" +
                "header=" + header +
                ", data=" + data +
                ", children=" + children +
                '}';
    }
}
