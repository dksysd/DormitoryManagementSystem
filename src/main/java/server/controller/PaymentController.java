package server.controller;

import server.persistence.dao.*;
import server.persistence.dto.*;
import server.util.ProtocolValidator;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static server.util.ProtocolValidator.getIdBySessionId;

public class PaymentController {

    /**
     * @param protocol header(type:request, dataType: TLV, code: GET_PAYMENT_AMOUNT, dataLength:)
     *                 data:
     *                 children< header(type: value, dataType: string, code: sessionId, dataLength:)
     *                 data:세션아이디 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength:
     * data:
     * children < header(type: value, dataType: int, code: PAYMENT_AMOUNT, dataLength:), data : 결제금액 >
     */

    public static Protocol<?> getPaymentAmount(Protocol<?> protocol) throws SQLException {
        Protocol<?> resProtocol = new Protocol<>();
        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        String sessionId = (String) protocol.getChildren().getFirst().getData();
        String id = getIdBySessionId(sessionId);

        if (id != null && ProtocolValidator.isStudent(sessionId)) {

            PaymentDAO paymentDAO = new PaymentDAO();
            PaymentDTO paymentDTO = paymentDAO.findByUid(id);

            if (paymentDTO != null) {
                int amount = paymentDTO.getPaymentAmount();

                Protocol<Integer> childProtocol = new Protocol<>(
                        new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.PAYMENT_AMOUNT, 0),
                        amount
                );

                resProtocol.addChild(childProtocol);
            } else {
                // 결제 정보가 없는 경우 처리
                header.setCode(Code.ResponseCode.ErrorCode.INVALID_VALUE);
            }

        } else {
            // 세션 ID가 유효하지 않은 경우
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
        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);

        String sessionId = (String) protocol.getChildren().getFirst().getData();
        String id = getIdBySessionId(sessionId);

        if (id != null&&ProtocolValidator.isStudent(sessionId)) {
            PaymentDAO paymentDAO = new PaymentDAO();
            PaymentDTO paymentDTO = paymentDAO.findByUid(id);

            if (paymentDTO != null) {
                String status = paymentDTO.getPaymentStatusDTO().getStatusName();

                Protocol<String> childProtocol = new Protocol<>(
                        new Header(Type.VALUE, DataType.STRING, Code.ValueCode.PAYMENT_STATUS_NAME, 0),
                        status
                );

                resProtocol.addChild(childProtocol);
            } else {
                // 결제 정보가 없는 경우 처리
                header.setCode(Code.ResponseCode.ErrorCode.INVALID_REQUEST);
            }
        } else {
            // 세션 ID가 유효하지 않은 경우 처리
            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        }

        resProtocol.setHeader(header);
        return resProtocol;
    }


    /**
     * @param protocol header(type:request, dataType: TLV, code: BANK_TRANSFER, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: accountNumber, dataLength:,)
     *                 data: 계좌번호),
     *                 2 ( header(type: value, dataType: string, code: accountHolderName, dataLength:,)
     *                 data: 계좌주이름 ),
     *                 3 ( header(type: value, dataType: string, code: bank_name, dataLength:,)
     *                 data: 은행명)
     *                 4 ( header(type: value, dataType: string, code: bank_code, dataLength:,)
     *      *                 data: 은행코드)
     *                 5 ( header(type: value, dataType: string, code: PAYMENT_STATUS_NAME, dataLength:,)
     *                 data: "납부")
     *                 6 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 ),
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
     * data: null
     */
    public static Protocol<?> payByBankTransfer(Protocol<?> protocol) throws SQLException {
        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        Protocol<?> resProtocol = new Protocol<>();
        BankTransferPaymentDAO BTPaymentDAO = new BankTransferPaymentDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        String id;
        String sessionId = (String) protocol.getChildren().getLast().getData();
        id = getIdBySessionId(sessionId);
        if (id != null&&ProtocolValidator.isStudent(sessionId)) {
            BTPaymentDAO.save(new BankTransferPaymentDTO(0, (String) protocol.getChildren().get(0).getData(),
                    (String) protocol.getChildren().get(1).getData(), LocalDateTime.now(), paymentDAO.findByUid(id),
                    new BankDTO(0, (String) protocol.getChildren().get(2).getData(), (String) protocol.getChildren().get(3).getData())));
            paymentDAO.statusUpdate(id, (String) protocol.getChildren().get(4).getData());
        } else header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        resProtocol.setHeader(header);
        return resProtocol;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: CARD_MOVEMENT, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: cardNumber, dataLength:,)
     *                 data: 카드번호),
     *                 2 ( header(type: value, dataType: string, code: card_issuer, dataLength:,)
     *                 data: 카드사 이름),
     *                 3 ( header(type: value, dataType: string, code: card_code, dataLength:,)
     *      *                 data: 카드사 코드),
     *                 4 ( header(type: value, dataType: string, code: PAYMENT_STATUS_NAME, dataLength:,)
     *                 data: "납부"),
     *                 5 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
     * data: null
     */
    public static Protocol<?> payByCard(Protocol<?> protocol) {
        Protocol<?> resProtocol = new Protocol<>();
        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        try {
            String sessionId = (String) protocol.getChildren().getLast().getData();
            String id = getIdBySessionId(sessionId);

            if (id == null && !ProtocolValidator.isStudent(sessionId)) {
                header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
                resProtocol.setHeader(header);
                return resProtocol;
            }
                PaymentDAO paymentDAO = new PaymentDAO();
                CardPaymentDAO cardPaymentDAO = new CardPaymentDAO();
                cardPaymentDAO.save(new CardPaymentDTO(0, (String) protocol.getChildren().get(0).getData(),
                        LocalDateTime.now(),
                        new CardIssuerDTO(0, (String) protocol.getChildren().get(1).getData(), (String) protocol.getChildren().get(2).getData()),
                        paymentDAO.findByUid(id)
                ));
                paymentDAO.statusUpdate(id, (String) protocol.getChildren().get(3).getData());


        }catch (SQLException e) {
            // 데이터베이스 예외 처리
            header.setCode(Code.ResponseCode.ErrorCode.INTERNAL_SERVER_ERROR);
        }

        resProtocol.setHeader(header);
        return resProtocol;
    }


    /**
     * @param protocol header(type:request, dataType: TLV, code: REFUND_REQUEST, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code:PAYMENT_STATUS, dataLength:,)
     *                 data: "환불" ),
     *                 2 ( header(type: value, dataType: string, code:Refund_reason, dataLength:,)
     *      *                 data: "환불 사유" ),
     *                 3 ( header(type: value, dataType: string, code:ACCOUNT_NUMBER, dataLength:,)
     *                 data: "계좌번호" ),
     *                 4 ( header(type: value, dataType: string, code:ACCOUNT_HOLDER_NAME, dataLength:,)
     *                 data: "계좌주 이름" ),
     *                 5 ( header(type: value, dataType: string, code:BANK_NAME, dataLength:,)
     *                 data: "은행명" ),
     *                 6 ( header(type: value, dataType: string, code:bank_code, dataLength:,)
     *      *                 data: "은행 코드" ),
     *                 7 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return protocol header(type:request, dataType: TLV, code: REFUND_REQUEST, dataLength:)
     * data:
     * children <
     * 1 ( header(type: value, dataType: string, code:REFUND_AMOUNT, dataLength:,)
     * data: "환불금액" ),
     */
    public static Protocol<?> requestRefund(Protocol<?> protocol) throws SQLException {
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        PaymentDAO paymentDAO = new PaymentDAO();

        Protocol<?> respProtocol = new Protocol<>(new Header(Type.VALUE, DataType.TLV, Code.ResponseCode.OK, 0), "");
        if (id != null&&ProtocolValidator.isStudent(sessionId)) {

            PaymentDTO paymentDTO = paymentDAO.findByUid(id);
            if (paymentDTO != null && Objects.equals(paymentDTO.getPaymentStatusDTO().getStatusName(), "납부")) {

                RoomAssignmentDAO roomAssignmentDAO = new RoomAssignmentDAO();
                MoveOutRequestDAO moveOutRequestDAO = new MoveOutRequestDAO();
                RoomAssignmentDTO roomAssignmentDTO = roomAssignmentDAO.findByUid(id);
                MoveOutRequestDTO moveOutRequestDTO = moveOutRequestDAO.findByUid(id);
                PaymentRefundDAO paymentRefundDAO = new PaymentRefundDAO();
                PaymentRefundDTO paymentRefundDTO = new PaymentRefundDTO(0,(String) protocol.getChildren().get(1).getData(), (String) protocol.getChildren().get(2).getData(),
                        (String) protocol.getChildren().get(3).getData(), LocalDateTime.now(),
                        new BankDTO(0, (String) protocol.getChildren().get(4).getData(),
                                (String) protocol.getChildren().get(5).getData()), paymentDTO);
                int refund_id = paymentRefundDAO.save(paymentRefundDTO);
                LocalDateTime start = roomAssignmentDTO.getMoveInAt();
                LocalDateTime moveOut = moveOutRequestDTO.getCheckoutAt();
                LocalDateTime end = moveOutRequestDTO.getCheckoutAt();
                long totalDays = ChronoUnit.DAYS.between(start, end);
                long remainingDays = ChronoUnit.DAYS.between(moveOut, end);

                int totalAmount = paymentDTO.getPaymentAmount();
                int refundAmount = (int) ((remainingDays / (double) totalDays) * totalAmount);
                paymentDTO.getPaymentStatusDTO().setStatusName("환불");
                paymentDAO.update(paymentDTO);
                paymentRefundDTO.setId(refund_id);
                respProtocol.addChild(new Protocol<>(new Header(Type.VALUE, DataType.INTEGER, Code.ValueCode.REFUND_AMOUNT, 0), refundAmount));
                moveOutRequestDTO.setPaymentRefundDTO(paymentRefundDTO);
                moveOutRequestDAO.update(moveOutRequestDTO);
            }
            respProtocol.getHeader().setCode(Code.ErrorCode.INVALID_REQUEST);
        } else respProtocol.getHeader().setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        return respProtocol;
    }


    /**
     * @param protocol header(type:request, dataType: TLV, code: REFUND_REQUEST, dataLength:)
     *                 data:
     *                 children <
     *                 header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : response, dataType : TLV, code : OK, dataLength :)
     * children<
     * header(type: value, dataType: string, code: REFUND_STATUS,dataLength:)
     * data: 환불 상태)
     * >
     */
    public static Protocol<?> getRefundStatus(Protocol<?> protocol) throws SQLException {
        String sessionId = (String) protocol.getChildren().getFirst().getData();
        String id = getIdBySessionId(sessionId);

        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        Protocol<String> resProtocol = new Protocol<>();

        if (id != null&&ProtocolValidator.isStudent(sessionId)) {
            PaymentDAO paymentDAO = new PaymentDAO();
            PaymentDTO paymentDTO = paymentDAO.findByUid(id);

            if (paymentDTO != null) {
                resProtocol.addChild(new Protocol<>(
                        new Header(Type.VALUE, DataType.STRING, Code.ValueCode.REFUND_STATUS, 0),
                        paymentDTO.getPaymentStatusDTO().getStatusName()
                ));
            } else {

                header.setCode(Code.ResponseCode.ErrorCode.INVALID_REQUEST);
            }
        } else {
            // 세션 ID가 유효하지 않은 경우 UNAUTHORIZED 처리
            header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
        }

        resProtocol.setHeader(header);
        return resProtocol;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: REFUND_CONFIRM, dataLength:)
     *                 data:
     *                 children <
     *                 header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : response, dataType : TLV, code : OK ( 오류면 에러코드), dataLength :)
     * data:
     * >
     */
//    public static Protocol<?> confirmRefund(Protocol<?> protocol) throws SQLException {
//        String sessionId = (String) protocol.getChildren().getFirst().getData();
//        String id = getIdBySessionId(sessionId);
//        if (id != null&&ProtocolValidator.isAdmin(sessionId)) {
//
//        }
//    }




}
