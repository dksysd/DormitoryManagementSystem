package server.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    public enum ContentType {

    }

    private String host;
    private ContentType contentType;
    private int bodyLength;
}
