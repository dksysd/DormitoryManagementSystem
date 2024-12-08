package server.controller;

import server.network.protocol.Header;
import server.network.protocol.TLV.Code;
import server.network.protocol.TLV.Protocol;
import server.persistence.dao.PaymentDAO;
import server.persistence.dao.PaymentStatusDAO;
import server.persistence.dao.UserDAO;
import server.persistence.dao.UserDAOI;
import server.persistence.dto.UserDTO;

import java.sql.SQLException;

public class PaymentController {
    /**
     * @param protocol
     *                 header(type:request, dataType: TLV, code: getPayment, dataLength:)
     *                 data:
     *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * data:
     * inner 1 header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:, data : 결제금액)
     */

    public static Protocol getPayment(Protocol protocol) throws SQLException {
        Header header = protocol.getHeader();
        PaymentDAO paymentDAO = new PaymentDAO();
        String id;
        int amount;
        if (validateHeader(protocol, header,Code.RequestCode.GET_PAYMENT) && validateDataLength(protocol, header)) {
            id = SessionIdController.getIdByIdBySessionId(header.getSessionId());
            amount = paymentDAO.getPaymentAmountByUid(id);
        }
        Protocol resProtocol = new Protocol();
        //protocol 생성 로직
     return resProtocol;}
    /**
     *
     * @param protocol
     * header(type:request, dataType: TLV, code: getPaymentStatus, dataLength:)
     *      *                 data:
     *      *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
     * @return
     * header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     *      * data:
     *      * inner 1 header(type: value, dataType: String, code: PAYMENT_STATUS, dataLength:, data : 결제상태)
     * @throws SQLException
     */
    public static Protocol getPaymentStatus(Protocol protocol) throws SQLException {
        Header header = protocol.getHeader();
        PaymentStatusDAO paymentStatusDAO = new PaymentStatusDAO();
        String id;
        id = SessionIdController.getIdByIdBySessionId(header.getSessionId());
        String status;
        if(validateHeader(protocol,header,code)&&validateDataLength(protocol,header){
        status = paymentStatusDAO.getPaymentStatusByUid(id);
        }
        Protocol resProtocol = new Protocol();
        return resProtocol;
    }


    private static boolean validateHeader(Protocol protocol, Header header,Code code) throws SQLException {


        if (header == null) {
            throw new IllegalArgumentException("헤더가 유효하지 않습니다.");
        }

        if (!header.getType().equals(Type.REQUEST) ||
                !header.getDataType().equals(DataType.TLV) ||
                !header.getCode().equals(code)) {
            throw new IllegalArgumentException("헤더 정보가 잘못되었습니다.");
        }

        return true; // 모든 조건을 통과하면 true 반환
    }

}

private static boolean validateDataLength(Protocol protocol, Header header) {
    byte[] data = (byte[]) protocol.getData();


    if (data == null || header == null) {
        throw new IllegalArgumentException("데이터 또는 헤더가 null입니다.");
    }

    int expectedLength = header.getDataLength();
    int actualLength = data.length;

    if (expectedLength != actualLength) {
        throw new IllegalArgumentException("데이터 길이가 유효하지 않습니다. " +
                "예상 길이: " + expectedLength +
                ", 실제 길이: " + actualLength);
    }

    return true; // 데이터 길이가 올바른 경우 true 반환
}
