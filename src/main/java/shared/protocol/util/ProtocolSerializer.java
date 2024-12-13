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

/**
 * ProtocolSerializer 클래스는 {@link Protocol} 객체를
 * 바이트 배열 형태로 직렬화하는 기능을 제공합니다.
 * 직렬화된 데이터를 {@link ByteBuffer}에 담아 반환하며,
 * 각 데이터 유형에 따라 데이터를 변환하고 처리합니다.
 */
public class ProtocolSerializer {

    /**
     * 주어진 {@link Protocol} 객체를 바이트 배열로 직렬화하여 {@link ByteBuffer} 객체로 반환합니다.
     * 헤더와 데이터를 모두 직렬화하며, 데이터 유형에 따라 적절한 직렬화 처리를 수행합니다.
     *
     * @param protocol 직렬화할 {@link Protocol} 객체
     * @return 직렬화된 {@link ByteBuffer} 객체
     * @throws ProtocolSerializeException 데이터가 null이거나 지원되지 않는 경우 발생
     */
    public static ByteBuffer serialize(Protocol<?> protocol) {
        Header header = protocol.getHeader();
        DataType dataType = header.getDataType();
        ByteBuffer dataBuffer = null;

        // 데이터 유형별 직렬화 처리
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
            case BOOLEAN -> {
                dataBuffer = ByteBuffer.allocate(1);
                boolean data = (boolean) checkDataIsNull(protocol.getData());
                dataBuffer.put((byte) (data ? 1 : 0));
                dataBuffer.flip();
            }
            case STRING -> {
                String data = (String) checkDataIsNull(protocol.getData());
                dataBuffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
            }
            case RAW -> {
                dataBuffer = ByteBuffer.wrap((byte[]) checkDataIsNull(protocol.getData()));
            }
            case TLV -> {
                int dataLength = 0;
                List<ByteBuffer> childrenBuffers = new ArrayList<>();

                // 하위 프로토콜 직렬화 처리
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

        // 헤더의 데이터 길이를 설정
        header.setDataLength(dataBuffer.capacity());

        // 헤더와 데이터 합쳐서 최종 버퍼 생성
        ByteBuffer buffer = ByteBuffer.allocate(Header.BYTES + header.getDataLength());
        buffer.put(header.getType().toByte());
        buffer.put(header.getDataType().toByte());
        buffer.put(header.getCode().toByte());
        buffer.putInt(header.getDataLength());
        buffer.put(dataBuffer);

        return buffer;
    }

    /**
     * 데이터가 null인지 검증하고, null인 경우 예외를 발생시킵니다.
     *
     * @param data 확인할 데이터
     * @param <T>  데이터의 타입
     * @return null이 아닌 데이터
     * @throws ProtocolSerializeException 데이터가 null인 경우 발생
     */
    private static <T> T checkDataIsNull(T data) {
        if (data == null) {
            throw new ProtocolSerializeException("Data is null");
        }
        return data;
    }
}
