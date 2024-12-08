package shared.protocol.persistence;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    public static final byte BYTES = 7;

    private Type type;
    private DataType dataType;
    private Code code;
    private int dataLength;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Header header)) return false;
        return dataLength == header.dataLength && type == header.type && dataType == header.dataType && Objects.equals(code, header.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, dataType, code, dataLength);
    }

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
