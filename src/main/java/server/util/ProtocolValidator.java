package server.util;

import shared.protocol.persistence.*;

import java.sql.SQLException;

public class ProtocolValidator {


    public static boolean validateHeader(Header header, Type type, DataType dataType, Code code) throws SQLException {


        if (header == null) {
            throw new IllegalArgumentException("헤더가 유효하지 않습니다.");
        }

        if (!header.getType().equals(type) ||
                !header.getDataType().equals(dataType) ||
                !header.getCode().equals(code)) {
            throw new IllegalArgumentException("헤더 정보가 잘못되었습니다.");
        }

        return true; // 모든 조건을 통과하면 true 반환
    }


    public static boolean validateDataLength(Protocol protocol, Header header) {
        byte[] data = (byte[]) protocol.getData();


        if (data == null || header == null) {
            throw new IllegalArgumentException("데이터 또는 헤더가 null입니다.");
        }

        int expectedLength = header.getDataLength();
        int actualLength = data.length;

        if (expectedLength != actualLength) {
            throw new IllegalArgumentException("데이터 길이가 유효하지 않습니다. " +
                    "예상 길이: " + expectedLength +
                    ", 실제 길이: " + actualLength);
        }

        return true; // 데이터 길이가 올바른 경우 true 반환
    }

    public static boolean verifySessionId(String sessionId) {

        //기타 로직 차후 구현
        return true;
    }

    public static String getIdByIdBySessionId(String sessionId) {
        String id = "";


        return id;
    }

}
