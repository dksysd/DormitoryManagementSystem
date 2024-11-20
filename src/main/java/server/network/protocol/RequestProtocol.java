package server.network.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RequestProtocol extends Protocol {
    public enum Method {
        GET,
        POST,
        PUT,
        DELETE,
        PATCH
    }

    private Method method;
    private String url;

    public RequestProtocol(Method method, String url, Header header, Body body) {
        super(header, body);
        this.method = method;
        this.url = url;
    }

    @Override
    public String toString() {
        String str = super.toString();
        return "RequestProtocol{" +
                "method=" + method +
                ", url='" + url + "\'," + str +
                '}';
    }
}
