package server.network.protocol.TLV;

public interface ByteEnum {
    static <T extends Enum<T>> byte toByte(Enum<T> e) {
        return (byte) e.ordinal();
    }
}
