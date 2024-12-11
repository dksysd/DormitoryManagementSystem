package server.core.handler;

import lombok.Getter;
import server.core.persistence.ThrowingFunction;
import server.exception.IllegalRequestException;
import shared.protocol.persistence.*;

import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    @Getter
    private static final RequestHandler INSTANCE = new RequestHandler();

    private final Map<Code.RequestCode, ThrowingFunction<Protocol<?>, Protocol<?>>> requestMap = new HashMap<>();

    private RequestHandler() {
    }

    public Protocol<?> request(Protocol<?> protocol) throws Exception {
        if (!(protocol.getHeader().getCode() instanceof Code.RequestCode code)) {
            throw new IllegalRequestException("Invalid request code " + protocol.getHeader().getCode());
        }
        ThrowingFunction<Protocol<?>, Protocol<?>> function = requestMap.get(code);
        if (function == null) {
            Protocol<Protocol<String>> response = new Protocol<>();
            Header header = new Header();
            header.setType(Type.ERROR);
            header.setCode(Code.ErrorCode.INVALID_REQUEST);
            header.setDataType(DataType.TLV);
            response.setHeader(header);

            Protocol<String> innerProtocol = new Protocol<>();
            Header innerHeader = new Header();
            innerHeader.setType(Type.VALUE);
            innerHeader.setCode(Code.ValueCode.MESSAGE);
            innerHeader.setDataType(DataType.STRING);
            innerProtocol.setHeader(innerHeader);
            innerProtocol.setData("RequestCode " + code + " not supported");
            response.addChild(innerProtocol);

            return response;
        }
        return function.apply(protocol);
    }

    public void addRequestHandler(Code.RequestCode code, ThrowingFunction<Protocol<?>, Protocol<?>> function) {
        INSTANCE.requestMap.put(code, function);
    }

    public void removeRequestHandler(Code.RequestCode code) {
        INSTANCE.requestMap.remove(code);
    }

    public void clearRequestHandlers() {
        INSTANCE.requestMap.clear();
    }
}
