package server.network.protocol;

import server.network.serialize.JsonSerializable;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringTokenizer;

public interface ProtocolSerializable {
    static byte[] serialize(Protocol protocol) throws Exception {
        byte[] bodyData = JsonSerializable.serialize(protocol.getBody());

        StringBuilder stringBuilder = new StringBuilder();
        switch (protocol) {
            case RequestProtocol requestProtocol -> {
                stringBuilder.append(requestProtocol.getMethod()).append(" ").append(requestProtocol.getUrl());
                protocol.getHeader().setBodyLength(bodyData.length);
            }

            case ResponseProtocol responseProtocol -> {
                stringBuilder.append(responseProtocol.getStatus());
                protocol.getHeader().setBodyLength(bodyData.length);
            }
            default -> throw new IllegalArgumentException("Unknown protocol");
        }
        stringBuilder.append(Protocol.Delimiter);
        headerToString(protocol.getHeader(), stringBuilder);
        stringBuilder.append(Protocol.Delimiter);

        byte[] otherData = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[otherData.length + bodyData.length];
        System.arraycopy(otherData, 0, data, 0, otherData.length);
        System.arraycopy(bodyData, 0, data, otherData.length, bodyData.length);
        return data;
    }

    private static void headerToString(Header header, StringBuilder stringBuilder) {
        stringBuilder.append(header.getHost()).append(" ");
        stringBuilder.append(header.getContentType()).append(" ");
        stringBuilder.append(header.getSessionId()).append(" ");
        stringBuilder.append(header.getBodyLength()).append(" ");
    }

    static <T extends Protocol> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        Protocol protocol = null;
        String string = new String(data, StandardCharsets.UTF_8);
        StringTokenizer stringTokenizer = new StringTokenizer(string, Protocol.Delimiter);
        StringTokenizer startLineTokenizer = new StringTokenizer(stringTokenizer.nextToken());
        StringTokenizer headerTokenizer = new StringTokenizer(stringTokenizer.nextToken());
        Header header = new Header(headerTokenizer.nextToken(), Header.ContentType.valueOf(headerTokenizer.nextToken()), headerTokenizer.nextToken(), Integer.parseInt(headerTokenizer.nextToken()));
        Body body = JsonSerializable.deserialize(stringTokenizer.nextToken().getBytes(StandardCharsets.UTF_8), Body.class);

        String firstWord = startLineTokenizer.nextToken();
        if (Arrays.stream(RequestProtocol.Method.values()).anyMatch(m -> m.name().equals(firstWord))) {
            RequestProtocol.Method method = RequestProtocol.Method.valueOf(firstWord);
            protocol = new RequestProtocol(method, startLineTokenizer.nextToken(), header, body);
        } else if (Arrays.stream(ResponseProtocol.Status.values()).anyMatch(m -> m.name().equals(firstWord))) {
            ResponseProtocol.Status status = ResponseProtocol.Status.valueOf(firstWord);
            protocol = new ResponseProtocol(status, header, body);
        }

        return clazz.cast(protocol);
    }
}
