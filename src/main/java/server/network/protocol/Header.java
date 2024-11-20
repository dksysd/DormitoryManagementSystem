package server.network.protocol;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Header {
    public enum ContentType {
        APPLICATION_JSON,
        MULTIPART_FORM_DATA,
    }

    private String host;
    private ContentType contentType;
    private int bodyLength;
}
