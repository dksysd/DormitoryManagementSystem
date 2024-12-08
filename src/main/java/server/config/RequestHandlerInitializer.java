package server.config;

import server.core.handler.RequestHandler;
import shared.protocol.persistence.*;

public interface RequestHandlerInitializer {
    static void init(RequestHandler requestHandler) {
        requestHandler.clearRequestHandlers();

        // todo 여기에 request 함수 mapping 하면 됨
        requestHandler.addRequestHandler(Code.RequestCode.LOGIN, (request) -> {
            Protocol<Protocol<String>> response = new Protocol<>();
            Header header = new Header();
            header.setType(Type.RESPONSE);
            header.setCode(Code.ResponseCode.OK);
            header.setDataType(DataType.TLV);
            response.setHeader(header);

            Protocol<String> innerProtocol = new Protocol<>();
            Header innerHeader = new Header();
            innerHeader.setType(Type.VALUE);
            innerHeader.setCode(Code.ValueCode.MESSAGE);
            innerHeader.setDataType(DataType.STRING);
            innerProtocol.setHeader(innerHeader);
            innerProtocol.setData("Hello World");
            response.addChild(innerProtocol);
            return response;
        });
    }
}
