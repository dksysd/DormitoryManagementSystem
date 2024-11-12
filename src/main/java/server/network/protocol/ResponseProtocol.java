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

        Continue(100, "Continue"),
        OK(200, "OK"), Created(201, "Created"),
        Accepted(202, "Accepted"),
        Rejected(203, "Rejected"),
        ResetContent(204, "Reset Content"),
        PartialContent(205, "Partial Content"),
        MultiStatus(206, "Multi Status"),
        MovedPermanently(301, "Moved Permanently"),
        Found(302, "Found"),
        NotModified(303, "Not Modified"),
        BadRequest(400, "Bad Request"),
        Unauthorized(401, "Unauthorized"),
        Forbidden(403, "Forbidden"),
        NotFound(404, "Not Found"),
        MethodNotAllowed(405, "Method Not Allowed"),
        NotAcceptable(406, "Not Acceptable"),
        InternalServerError(500, "Internal Server Error"),
        BadGateway(502, "Bad Gateway"),
        ServiceUnavailable(503, "Service Unavailable"),
        GatewayTimeout(504, "Gateway Timeout");
        private final Integer code;
        private final String message;
    }

    private Status status;

    public ResponseProtocol(Header header, Body body) {
        super(header, body);
    }
}
