package server.controller;

import server.persistence.dao.BankTransferPaymentDAO;
import server.persistence.dao.CardPaymentDAO;
import server.persistence.dao.PaymentDAO;
import server.persistence.dto.BankDTO;
import server.persistence.dto.BankTransferPaymentDTO;
import server.persistence.dto.CardIssuerDTO;
import server.persistence.dto.PaymentDTO;
import shared.protocol.persistence.*;

import java.sql.SQLException;

import static server.util.ProtocolValidator.getIdByIdBySessionId;

public class PaymentController {

    /**
     * @param protocol header(type:request, dataType: TLV, code: getPayment, dataLength:)
     *                 data:
     *                 children< header(type: value, dataType: string, code: sessionId, dataLength:)
     *                 data:세션아이디 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * data:
     * children < header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:), data : 결제금액 >
     */

    public static Protocol<?> getPaymentAmount(Protocol<?> protocol) throws SQLException {
        Protocol<?> resProtocol = new Protocol<>();
        Protocol<Integer> childProtocol = new Protocol<>();
        Header header = new Header();
        Header childHeader = childProtocol.getHeader();
        PaymentDAO paymentDAO = new PaymentDAO();
        PaymentDTO paymentDTO;
        String id;
        int amount;

        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
        header.setType(Type.RESPONSE);
        header.setDataType(DataType.TLV);
        if (id != null) {
            paymentDTO = paymentDAO.findByUid(id);
            amount = paymentDTO.getPaymentAmount();
            header.setCode(Code.ResponseCode.OK);
            childHeader.setType(Type.VALUE);
            childHeader.setDataType(DataType.INTEGER);
            childHeader.setCode(Code.ResponseCode.ValueCode.PAYMENT_AMOUNT);
            childProtocol.setHeader(childHeader);
            childProtocol.setData(amount);
            protocol.addChild(childProtocol);
        } else {
            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        }
        resProtocol.setHeader(header);
        return resProtocol;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: getPaymentStatus, dataLength:)
     *                 data:
     *                 children< header(type: value, dataType: string, code: sessionId, dataLength:,
     *                 data:세션아이디 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * data:
     * children< header(type: value, dataType: String, code: PAYMENT_STATUS, dataLength:,
     * data : 결제상태 >
     */
    public static Protocol<?> getPaymentStatus(Protocol<?> protocol) throws SQLException {
        Protocol<?> resProtocol = new Protocol<>();
        Protocol<String> childProtocol = new Protocol<>();
        Header header = new Header();
        Header childHeader = childProtocol.getHeader();
        PaymentDAO paymentDAO = new PaymentDAO();
        PaymentDTO paymentDTO;
        String id;
        String status;

        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
        header.setType(Type.RESPONSE);
        header.setDataType(DataType.TLV);
        if (id != null) {
            paymentDTO = paymentDAO.findByUid(id);
            status = paymentDTO.getPaymentStatusDTO().getStatusName();
            header.setCode(Code.ResponseCode.OK);
            childHeader.setType(Type.VALUE);
            childHeader.setDataType(DataType.STRING);
            childHeader.setCode(Code.ResponseCode.ValueCode.PAYMENT_STATUS_NAME);
            childProtocol.setHeader(childHeader);
            childProtocol.setData(status);
            protocol.addChild(childProtocol);
        } else {
            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        }
        resProtocol.setHeader(header);
        return resProtocol;
    }


    /**
     * @param protocol header(type:request, dataType: TLV, code: payByBankTransfer, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 ),
     *                 2 ( header(type: value, dataType: string, code: accountNumber, dataLength:,)
     *                 data: 계좌번호),
     *                 3 ( header(type: value, dataType: string, code: bankName, dataLength:,)
     *                 data: 은행명),
     *                 4 ( header(type: value, dataType: string, code: accountHolderName, dataLength:,)
     *                 data: 계좌주이름 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
     * data: null
     */
    public static Protocol<?> payByBankTransfer(Protocol<?> protocol) {
        Header header = new Header();
        Protocol<?> resProtocol = new Protocol<>();
        BankTransferPaymentDAO BTpaymentDAO = new BankTransferPaymentDAO();
        BankTransferPaymentDTO BTpaymentDTO = new BankTransferPaymentDTO();
        BankDTO bankDTO = new BankDTO();
        String id;
        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
        header.setType(Type.RESPONSE);
        header.setDataType(DataType.TLV);
        if (id != null) {
            /* todo uid로 update를 만드시는데 accountNumber, bankName, accountHolderName,paymentStatusName 만 update하고싶어요
             * NOTE : accountNumber, bankName은 BankTransferPaymentDAO에서 바꿀 수 있지만 나머지는 나머지에 해당하는 친구들을 update하는게
             * 설계 상 이뻐보여요(자기가 갖고 있는걸 수정). 그렇게 수정함.
             * accountNumber, accountHolderName, bankName : BTpaymentDAO.update(String uid, String accountNumber, String accountHolderName, String bankName)
             * paymentStatusName : PaymentDAO.statusUpdate(String uid, String PaymentStatusName)
             * uid 따로 따로 똑같은 값 입력해야한다는 단점이 있으나, 설계 깔끔함.
             */
            // BTpaymentDAO.update();


        }
    }


    public static Protocol<?> payByCard(Protocol<?> protocol) {
        Header header = protocol.getHeader();
        CardPaymentDAO CMpaymentDAO = new CardPaymentDAO();

        PaymentDTO paymentDTO = new PaymentDTO();
        CardIssuerDTO cardIssuerDTO = new CardIssuerDTO();
/**todo update(uid) cardNumber, cardIssuerName, paymentStatus 변경
 *
 */
    }


}
