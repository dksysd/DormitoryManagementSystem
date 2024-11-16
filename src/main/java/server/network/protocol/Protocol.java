package server.network.protocol;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public abstract class Protocol {
    public static final String Delimiter = "\r\n\r\n";
    public static final byte[] DelimiterBytes = Delimiter.getBytes();

    private final Header header;
    private final Body body;

    public Protocol() {
        header = new Header();
        body = new Body();
    }
}
