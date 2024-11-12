package server.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseProtocol extends Protocol {
    @Getter
    @AllArgsConstructor
    public enum Status {
        OK(200, "OK");

        private final Integer code;
        private final String message;
    }

    private Status status;

    public ResponseProtocol(Header header, Body body) {
        super(header, body);
    }
}
