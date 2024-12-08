package shared.protocol.persistence;

import server.exception.IllegalHeaderElementException;

public interface HeaderElement {
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

    static  <T extends Enum<T> & HeaderElement> T toHeaderElement(byte b, Class<T> type) {
        return type.getEnumConstants()[b];
    }
}
