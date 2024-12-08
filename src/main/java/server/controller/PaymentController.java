//package server.controller;
//
//
//import server.persistence.dao.BankTransferPaymentDAO;
//import server.persistence.dao.CardPaymentDAO;
//import server.persistence.dao.PaymentDAO;
//import server.persistence.dao.PaymentStatusDAO;
//import server.persistence.dto.BankDTO;
//import server.persistence.dto.BankTransferPaymentDTO;
//import server.persistence.dto.CardIssuerDTO;
//import server.persistence.dto.PaymentDTO;
//import shared.protocol.persistence.*;
//
//import javax.xml.transform.OutputKeys;
//import java.sql.SQLException;
//
//import static server.util.ProtocolValidator.*;
//
//public class PaymentController {
//    /**
//     * @param protocol header(type:request, dataType: TLV, code: getPayment, dataLength:)
//     *                 data:
//     *                 children< header(type: value, dataType: string, code: sessionId, dataLength:)
//     *                 data:세션아이디 >
//     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
//     *                  data:
//     *                  children < header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:), data : 결제금액 >
//     */
//
//    public static Protocol<?> getPaymentAmount(Protocol<?> protocol) throws SQLException {
//        Protocol<?> resProtocol = new Protocol<>();
//        Protocol<Integer> childProtocol = new Protocol<>();
//        Header header = protocol.getHeader();
//        Header childHeader = childProtocol.getHeader();
//        PaymentDAO paymentDAO = new PaymentDAO();
//        PaymentDTO paymentDTO;
//        String id;
//        int amount;
//
//        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
//        header.setType(Type.RESPONSE);
//        header.setDataType(DataType.TLV);
//        if(id!=null) {
//            paymentDTO = paymentDAO.findByUid(id);
//            amount = paymentDTO.getPaymentAmount();
//            header.setCode(Code.ResponseCode.OK);
//            childHeader.setType(Type.VALUE);
//            childHeader.setDataType(DataType.INTEGER);
//            childHeader.setCode(Code.ResponseCode.ValueCode.PAYMENT_AMOUNT);
//            childProtocol.setHeader(childHeader);
//            childProtocol.setData(amount);
//            protocol.addChild(childProtocol);
//        }else{
//            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
//        }
//        resProtocol.setHeader(header);
//        return resProtocol;
//    }
//
//    /**
//     * @param protocol header(type:request, dataType: TLV, code: getPaymentStatus, dataLength:)
//     *                 *                 data:
//     *                 *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
//     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
//     * * data:
//     * * inner 1 header(type: value, dataType: String, code: PAYMENT_STATUS, dataLength:, data : 결제상태)
//     * @throws SQLException
//     */
//    public static Protocol<?> getPaymentStatus(Protocol<?> protocol) throws SQLException {
//        Protocol<?> resProtocol = new Protocol<>();
//        Protocol<Integer> childProtocol = new Protocol<>();
//        Header header = protocol.getHeader();
//        Header childHeader = childProtocol.getHeader();
//        PaymentDAO paymentDAO = new PaymentDAO();
//        PaymentDTO paymentDTO;
//        String id;
//        String status;
//
//        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
//        header.setType(Type.RESPONSE);
//        header.setDataType(DataType.TLV);
//        if(id!=null) {
//            paymentDTO = paymentDAO.findByUid(id);
//            status = paymentDTO.getPaymentStatusDTO()
//            header.setCode(Code.ResponseCode.OK);
//            childHeader.setType(Type.VALUE);
//            childHeader.setDataType(DataType.INTEGER);
//            childHeader.setCode(Code.ResponseCode.ValueCode.PAYMENT_AMOUNT);
//            childProtocol.setHeader(childHeader);
//            childProtocol.setData(amount);
//            protocol.addChild(childProtocol);
//        }else{
//            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
//        }
//        resProtocol.setHeader(header);
//        return resProtocol;
//    }
//
//
//}
//
///**
// * @param protocol header(type:request, dataType: TLV, code: payByBankTransfer, dataLength:)
// *                 data:
// *                 list< 1 header(type: value, dataType: string, code: sessionId, dataLength:,)
// *                 data: 세션아이디
// *                 2 header(type: value, dataType: string, code: accountNumber, dataLength:,)
// *                 data: 계좌번호
// *                 3 header(type: value, dataType: string, code: bankName, dataLength:,)
// *                 data: 은행명
// *                 4 header(type: value, dataType: string, code: accountHolderName, dataLength:,)
// *                 data: 계좌주이름 >
// *
// * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
// * data:
// */
//public static Protocol payByBankTransfer(Protocol protocol) {
//    Header header = protocol.getHeader();
//    BankTransferPaymentDAO BTpaymentDAO = new BankTransferPaymentDAO();
//    BankTransferPaymentDTO BTpaymentDTO = new BankTransferPaymentDTO();
//    PaymentDTO paymentDTO = new PaymentDTO();
//    BankDTO bankDTO = new BankDTO();
//
//}
//
//public static Protocol payByCard(Protocol protocol) {
//    Header header = protocol.getHeader();
//    CardPaymentDAO CMpaymentDAO = new CardPaymentDAO();
//    CardPaymentDAO BTpaymentDTO = new CardPaymentDAO();
//    PaymentDTO paymentDTO = new PaymentDTO();
//    CardIssuerDTO cardIssuerDTO = new CardIssuerDTO();
//
//}
//
//
////package server.controller;
////
////import server.network.protocol.Header;
////import server.network.protocol.TLV.Code;
////import server.network.protocol.TLV.Protocol;
////import server.network.protocol.TLV.Type;
////import server.persistence.dao.PaymentDAO;
////import server.persistence.dao.PaymentStatusDAO;
////
////import java.sql.SQLException;
////
////import static server.util.ProtocolValidator.*;
////
////public class PaymentController {
////
////    /**
////     * @param protocol header(type:request, dataType: TLV, code: getPayment, dataLength:)
////     *                 data:
////     *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:,data:세션아이디 )
////     * @return Protocol header(type : Response, dataType : TLV, code : OK (틀리면 에러), dataLength:)
////     *                  data:
////     *                  inner 1 header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:, data: 결제금액)
////     */
////    public static Protocol getPaymentAmount(Protocol protocol) {
////        Header header = protocol.getHeader();
////        PaymentDAO paymentDAO = new PaymentDAO();
////        Protocol resProtocol = new Protocol();
////
////        try {
////            if (!validateHeader(protocol, header, Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT) ||
////                    !validateDataLength(protocol, header)) {
////                return createErrorResponse(Code.ErrorCode.INVALID_REQUEST);
////            }
////
////            String id = getIdByIdBySessionId(header.getSessionId());
////            int amount = paymentDAO.getPaymentAmountByUid(id);
////
////            // 성공 응답 생성(우선 그냥 해 놓음)
////            resProtocol.setHeader(createResponseHeader(header, Code.ResponseCode.OK));
////            resProtocol.setData(createDataProtocol(amount, Code.ResponseCode.PAYMENT_AMOUNT));
////        } catch (Exception e) {
////            // 에러 응답 생성
////            return createErrorResponse(Code.ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
////        }
////
////        return resProtocol;
////    }
////
////    /**
////     * @param protocol header(type:request, dataType: TLV, code: getPaymentStatus, dataLength:)
////     *                 data:
////     *                 inner 1 header(type: value, dataType: string, code: sessionId, dataLength:, data:세션아이디 )
////     * @return Protocol header(type : Response, dataType : TLV, code : OK (틀리면 에러), dataLength:)
////     *                  data:
////     *                  inner 1 header(type: value, dataType: String, code: PAYMENT_STATUS, dataLength:, data: 결제상태)
////     */
////    public static Protocol getPaymentStatus(Protocol protocol) {
////        Header header = protocol.getHeader();
////        PaymentStatusDAO paymentStatusDAO = new PaymentStatusDAO();
////        Protocol resProtocol = new Protocol();
////
////        try {
////            if (!validateHeader(protocol, header, Type.REQUEST, DataType.TLV, Code.RequestCode.GET_PAYMENT_STATUS) ||
////                    !validateDataLength(protocol, header)) {
////                return createErrorResponse(Code.ErrorCode.INVALID_REQUEST);
////            }
////
////            String id = getIdByIdBySessionId(header.getSessionId());
////            String status = paymentStatusDAO.getPaymentStatusByUid(id);
////
////            // 성공 응답 생성
////            resProtocol.setHeader(createResponseHeader(header, Code.ResponseCode.OK));
////            resProtocol.setData(createDataProtocol(status, Code.ResponseCode.PAYMENT_STATUS));
////        } catch (Exception e) {
////            // 에러 응답 생성
////            return createErrorResponse(Code.ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
////        }
////
////        return resProtocol;
////    }
////
////    // Helper method: 응답 Header 생성(Big Header)
////    private static Header createResponseHeader(Header requestHeader, Code.ResponseCode code, int dataLength) {
////        Header responseHeader = new Header();
////        responseHeader.setType(Type.RESPONSE);
////        responseHeader.setDataType(DataType.TLV);
////        responseHeader.setCode(code);
////        responseHeader.setDataLength(dataLength);
////        return responseHeader;
////    }
////
////    // Helper method: 응답의 Data 생성(inner Protocol이 있을 때)
////    private static Protocol createResponseData(Object data, enum dataType, Code.ResponseCode code) {
////        Protocol dataProtocol = new Protocol();
////        Header dataHeader = new Header();
////        dataHeader.setType(Type.VALUE);
////        dataHeader.setDataType();
////        dataHeader.setCode(code);
////        dataProtocol.setHeader(dataHeader);
////        dataProtocol.setData(data);
////        return dataProtocol;
////    }
//
//
//}
//
