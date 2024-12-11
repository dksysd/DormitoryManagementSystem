package shared.protocol.util;

import shared.protocol.persistence.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ProtocolParser {
    public static Header parseHeader(ByteBuffer headerBuffer) {
        Type type = HeaderElement.toHeaderElement(headerBuffer.get(), Type.class);
        DataType dataType = HeaderElement.toHeaderElement(headerBuffer.get(), DataType.class);
        Code code = switch (type) {
            case REQUEST -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.RequestCode.class);
            case RESPONSE -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ResponseCode.class);
            case ERROR -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ErrorCode.class);
            case VALUE -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ValueCode.class);
        };
        int dataLength = headerBuffer.getInt();

        return new Header(type, dataType, code, dataLength);
    }

    public static Protocol<?> parseProtocol(Header header, ByteBuffer dataBuffer) {
        return switch (header.getDataType()) {
            case INTEGER -> new Protocol<>(header, dataBuffer.getInt());
            case FLOAT -> new Protocol<>(header, dataBuffer.getFloat());
            case DOUBLE -> new Protocol<>(header, dataBuffer.getDouble());
            case BOOLEAN -> new Protocol<>(header, dataBuffer.get() == 1);
            case STRING -> {
                byte[] data = new byte[header.getDataLength()];
                dataBuffer.get(data);
                yield new Protocol<>(header, new String(data, StandardCharsets.UTF_8));
            }
            case RAW -> {
                byte[] data = new byte[header.getDataLength()];
                dataBuffer.get(data);
                yield new Protocol<>(header, data);
            }
            case TLV -> {
                Protocol<Protocol<?>> protocol = new Protocol<>();
                protocol.setHeader(header);
                if (header.getDataLength() == 0) {
                    yield protocol;
                } else {
                    while (dataBuffer.position() < header.getDataLength()) {
                        Header innerHeader = parseHeader(dataBuffer);
                        Protocol<?> innerProtocol = parseProtocol(innerHeader, dataBuffer);
                        protocol.addChild(innerProtocol);
                    }
                    yield protocol;
                }
            }
        };
    }
}

