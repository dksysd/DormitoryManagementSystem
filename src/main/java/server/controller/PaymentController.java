package server.controller;

import server.network.protocol.Header;
import server.network.protocol.TLV.Protocol;

public class PaymentController {
    /**
     *
     * @param protocol
     * getPayment()
     * header(type:request, dataType: TLV, code: getPayment, dataLength:)
     * data:
     * inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
     * 끝
     *
     * @return
     * header(type: Response, dataType: TLV, code: OK(틀리면 에러) dataLength:
     * data:
     * inner 1 header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:, data : 결제금액)
     */
    public static Protocol getPaymentStatus(Protocol protocol) {
Header header = protocol.getHeader();

if(SessionIdController.verifySessionId(header.getSessionId())){

};
}

}
