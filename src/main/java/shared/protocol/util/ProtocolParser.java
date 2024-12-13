package shared.protocol.util;

import server.exception.IllegalHeaderElementException;
import shared.protocol.persistence.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * ProtocolParser 클래스는 {@link Protocol} 및 {@link Header} 객체를
 * 파싱(구문 분석)하여 바이트 데이터로부터 프로토콜 메시지를 재구성하는 유틸리티를 제공합니다.
 * 전송된 데이터를 {@link ByteBuffer}로 읽어 헤더와 본문 데이터를 파싱하여 적절한 엔티티로 변환합니다.
 */
public class ProtocolParser {

    /**
     * 바이트 버퍼에서 {@link Header} 객체를 파싱합니다.
     * 초기 7 바이트를 기반으로 헤더의 유형, 데이터 타입, 코드, 데이터 길이를 해석합니다.
     *
     * @param headerBuffer 헤더 데이터를 포함한 {@link ByteBuffer}
     * @return 파싱된 {@link Header} 객체
     * @throws IllegalHeaderElementException 헤더 요소 파싱 중 유효하지 않은 값이 발생할 경우 예외 발생
     */
    public static Header parseHeader(ByteBuffer headerBuffer) throws IllegalHeaderElementException {
        // 헤더 유형을 파싱
        Type type = HeaderElement.toHeaderElement(headerBuffer.get(), Type.class);

        // 데이터 타입을 파싱
        DataType dataType = HeaderElement.toHeaderElement(headerBuffer.get(), DataType.class);

        // 헤더의 코드 값을 유형에 따라 파싱
        Code code = switch (type) {
            case REQUEST -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.RequestCode.class);
            case RESPONSE -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ResponseCode.class);
            case ERROR -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ErrorCode.class);
            case VALUE -> HeaderElement.toHeaderElement(headerBuffer.get(), Code.ValueCode.class);
        };

        // 데이터 길이 정보 파싱
        int dataLength = headerBuffer.getInt();

        return new Header(type, dataType, code, dataLength);
    }

    /**
     * {@link Header}와 {@link ByteBuffer} 데이터를 기반으로 {@link Protocol} 객체를 파싱합니다.
     * 데이터 타입에 따라 데이터를 적절한 Java 객체로 변환하여 프로토콜 객체를 생성합니다.
     *
     * @param header 파싱된 헤더 객체
     * @param dataBuffer 데이터 내용을 저장하는 {@link ByteBuffer}
     * @return 파싱된 {@link Protocol} 객체
     * @throws IllegalHeaderElementException 데이터 타입별 유효하지 않은 값이 발생할 경우 예외 발생
     */
    public static Protocol<?> parseProtocol(Header header, ByteBuffer dataBuffer) throws IllegalHeaderElementException {
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
                    // 데이터 길이만큼 반복적으로 하위 프로토콜을 파싱
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
