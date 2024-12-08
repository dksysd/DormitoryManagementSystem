package shared.protocol.util;

import shared.protocol.exception.ProtocolSerializeException;
import shared.protocol.persistence.DataType;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProtocolSerializer {
    public static ByteBuffer serialize(Protocol<?> protocol) {
        Header header = protocol.getHeader();
        DataType dataType = header.getDataType();

        ByteBuffer dataBuffer = null;
        switch (dataType) {
            case INTEGER -> {
                dataBuffer = ByteBuffer.allocate(Integer.BYTES);
                dataBuffer.putInt((int) checkDataIsNull(protocol.getData()));
                dataBuffer.flip();
            }
            case FLOAT -> {
                dataBuffer = ByteBuffer.allocate(Float.BYTES);
                dataBuffer.putFloat((float) checkDataIsNull(protocol.getData()));
                dataBuffer.flip();
            }
            case DOUBLE -> {
                dataBuffer = ByteBuffer.allocate(Double.BYTES);
                dataBuffer.putDouble((double) checkDataIsNull(protocol.getData()));
                dataBuffer.flip();
            }
            case STRING -> {
                String data = (String) checkDataIsNull(protocol.getData());
                dataBuffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
            }
            case RAW -> {
                dataBuffer = ByteBuffer.wrap((byte[]) checkDataIsNull(protocol.getData()));
                dataBuffer.flip();
            }
            case TLV -> {
                int dataLength = 0;
                List<ByteBuffer> childrenBuffers = new ArrayList<>();
                for (Protocol<?> child : protocol.getChildren()) {
                    ByteBuffer childBuffer = serialize(child);
                    childrenBuffers.add(childBuffer);
                    dataLength += childBuffer.capacity();
                }

                dataBuffer = ByteBuffer.allocate(dataLength);
                for (ByteBuffer childBuffer : childrenBuffers) {
                    childBuffer.flip();
                    dataBuffer.put(childBuffer);
                }

                dataBuffer.flip();
            }
        }

        header.setDataLength(dataBuffer.capacity());

        ByteBuffer buffer = ByteBuffer.allocate(Header.BYTES + header.getDataLength());
        buffer.put(header.getType().toByte());
        buffer.put(header.getDataType().toByte());
        buffer.put(header.getCode().toByte());
        buffer.putInt(header.getDataLength());
        buffer.put(dataBuffer);

        return buffer;
    }

    private static <T> T checkDataIsNull(T data) {
        if (data == null) {
            throw new ProtocolSerializeException("Data is null");
        }
        return data;
    }
}
