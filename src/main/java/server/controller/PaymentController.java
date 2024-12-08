package server.controller;

import server.network.protocol.Header;
import server.network.protocol.TLV.Code;
import server.network.protocol.TLV.Protocol;
import server.network.protocol.TLV.Type;
import server.persistence.dao.PaymentDAO;
import server.persistence.dao.PaymentStatusDAO;

import java.sql.SQLException;

import static server.util.ProtocolValidator.*;

public class PaymentController {
    /**
     * @param protocol header(type:request, dataType: TLV, code: getPayment, dataLength:)
     *                 data:
     *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * data:
     * inner 1 header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:, data : 결제금액)
     */

    public static Protocol getPaymentAmount(Protocol protocol) throws SQLException {
        Header header = protocol.getHeader();
        PaymentDAO paymentDAO = new PaymentDAO();
        String id;
        int amount;
        if (validateHeader(header, Code.RequestCode.GET_PAYMENT) && validateDataLength(protocol, header)) {
            id = getIdByIdBySessionId(header.getSessionId());
            amount = paymentDAO.getPaymentAmountByUid(id);
        }
        Protocol resProtocol = new Protocol();
        //protocol 생성 로직
        return resProtocol;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: getPaymentStatus, dataLength:)
     *                 *                 data:
     *                 *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * * data:
     * * inner 1 header(type: value, dataType: String, code: PAYMENT_STATUS, dataLength:, data : 결제상태)
     * @throws SQLException
     */
    public static Protocol getPaymentStatus(Protocol protocol) throws SQLException {
        Header header = protocol.getHeader();
        PaymentStatusDAO paymentStatusDAO = new PaymentStatusDAO();
        String id;
        id = getIdByIdBySessionId(header.getSessionId());
        String status;
        if (validateHeader(protocol, header, Type.REQUEST, DataType.TLV, code)
                && validateDataLength(protocol, header)) {
            status = paymentStatusDAO.getPaymentStatusByUid(id);
        }
        Protocol resProtocol = new Protocol();
        //protocol 생성 로직
        return resProtocol;
    }


}

