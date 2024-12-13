package server.core.handler;

import lombok.Getter;
import server.core.persistence.ThrowingFunction;
import server.exception.IllegalRequestException;
import shared.protocol.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * RequestHandler 클래스는 클라이언트 요청의 처리 메커니즘을 관리합니다.
 * 요청 코드와 처리 함수의 매핑을 설정하거나 제거하며, 클라이언트에서 전송된 요청을 해당 핸들러로 라우팅합니다.
 * 이 클래스는 싱글톤 패턴을 구현하여 전역적으로 단일 인스턴스를 제공합니다.
 */
public class RequestHandler {

    /**
     * RequestHandler의 싱글톤 인스턴스
     */
    @Getter
    private static final RequestHandler INSTANCE = new RequestHandler();

    /**
     * 요청 코드와 처리 함수의 매핑을 저장하는 맵
     */
    private final Map<Code.RequestCode, ThrowingFunction<Protocol<?>, Protocol<?>>> requestMap = new HashMap<>();

    /**
     * RequestHandler의 기본 생성자.
     * 외부에서 호출을 방지하여 싱글톤 패턴을 유지합니다.
     */
    private RequestHandler() {
    }

    /**
     * 클라이언트로부터 받은 요청을 처리합니다.
     * 요청의 헤더에 포함된 요청 코드에 따라 매핑된 핸들러를 호출합니다.
     * 해당하는 핸들러가 없을 경우, 에러 응답을 반환합니다.
     *
     * @param protocol 클라이언트 요청 {@link Protocol} 객체
     * @return 처리된 응답 {@link Protocol} 객체
     * @throws Exception 요청 처리 중 발생하는 예외
     */
    public Protocol<?> request(Protocol<?> protocol) throws Exception {
        // 요청 코드가 유효한지 확인
        if (!(protocol.getHeader().getCode() instanceof Code.RequestCode code)) {
            throw new IllegalRequestException("Invalid request code " + protocol.getHeader().getCode());
        }

        // 요청 코드에 매핑된 핸들러 호출
        ThrowingFunction<Protocol<?>, Protocol<?>> function = requestMap.get(code);
        if (function == null) {
            // 핸들러가 없을 경우 에러 응답 생성
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

    /**
     * 요청 코드를 지정하고, 해당 요청에 대한 처리 함수를 추가합니다.
     *
     * @param code     요청 코드 {@link Code.RequestCode}
     * @param function 요청을 처리할 함수 {@link ThrowingFunction}
     */
    public void addRequestHandler(Code.RequestCode code, ThrowingFunction<Protocol<?>, Protocol<?>> function) {
        INSTANCE.requestMap.put(code, function);
    }

    /**
     * 요청 코드에 매핑된 핸들러를 제거합니다.
     *
     * @param code 요청 코드 {@link Code.RequestCode}
     */
    public void removeRequestHandler(Code.RequestCode code) {
        INSTANCE.requestMap.remove(code);
    }

    /**
     * 모든 요청 코드를 처리하는 핸들러를 제거합니다.
     */
    public void clearRequestHandlers() {
        INSTANCE.requestMap.clear();
    }
}