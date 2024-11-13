package server.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import server.network.serialize.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Header implements Serializable {

    public enum ContentType {
        APPLICATION_JSON,
        MULTIPART_FORM_DATA,
    }

    private String host;
    private ContentType contentType;
    private int bodyLength;
}
