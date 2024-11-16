package server.network.handler;

import server.network.protocol.Protocol;

public class RequestProtocolHandler implements Handler {
    private final byte[] data;
    private final Protocol protocol;

    public RequestProtocolHandler(byte[] data, Protocol protocol) {
        this.data = data;
        this.protocol = protocol;
    }

    @Override
    public void handle() {
        handleProtocol(protocol);
    }

    private void handleProtocol(Protocol protocol) {
        int startLineDelimiterIndex = findDelimiter(data, 0);
        int requestHeaderDelimiterIndex = findDelimiter(data, startLineDelimiterIndex + 1);
        int actualBodyLength = data.length - requestHeaderDelimiterIndex;
        if (protocol.getHeader().getBodyLength() != actualBodyLength) {
            throw new IllegalArgumentException("Request body length does not match actual body length");
        }
    }

    private int findDelimiter(byte[] data, int pos) {
        int maxLength = data.length - Protocol.DelimiterBytes.length;
        for (int i = pos; i < maxLength; i++) {
            boolean isFound = true;
            for (int j = 0; j < Protocol.DelimiterBytes.length; j++) {
                if (data[i + j] != Protocol.DelimiterBytes[j]) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return i;
            }
        }
        return -1;
    }
}
