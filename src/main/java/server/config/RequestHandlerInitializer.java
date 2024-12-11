package server.config;

import server.controller.*;
import server.core.handler.RequestHandler;
import shared.protocol.persistence.*;

public interface RequestHandlerInitializer {
    static void init(RequestHandler requestHandler)  {
        requestHandler.clearRequestHandlers();

//        // todo 여기에 request 함수 mapping 하면 됨
//        requestHandler.addRequestHandler(Code.RequestCode.LOGIN, (request) -> {
//            Protocol<Protocol<String>> response = new Protocol<>();
//            Header header = new Header();
//            header.setType(Type.RESPONSE);
//            header.setCode(Code.ResponseCode.OK);
//            header.setDataType(DataType.TLV);
//            response.setHeader(header);
//
//            Protocol<String> innerProtocol = new Protocol<>();
//            Header innerHeader = new Header();
//            innerHeader.setType(Type.VALUE);
//            innerHeader.setCode(Code.ValueCode.MESSAGE);
//            innerHeader.setDataType(DataType.STRING);
//            innerProtocol.setHeader(innerHeader);
//            innerProtocol.setData("Hello World");
//            response.addChild(innerProtocol);
//            return response;
//        });
        requestHandler.addRequestHandler(Code.RequestCode.LOGIN,AuthController::login);
        requestHandler.addRequestHandler(Code.RequestCode.LOGOUT, AuthController::logout);
        requestHandler.addRequestHandler(Code.RequestCode.REFRESH_SESSION, AuthController::refreshSession);//authController

        requestHandler.addRequestHandler(Code.RequestCode.GET_USER_INFO, UserController::getUserInfo);//userController

        requestHandler.addRequestHandler(Code.RequestCode.GET_SELECTION_SCHEDULE, DormitoryUserController::getSelectionSchedule);
        requestHandler.addRequestHandler(Code.RequestCode.GET_MEAL_PLAN, DormitoryUserController::getMealPlan);
        requestHandler.addRequestHandler(Code.RequestCode.GET_DORMITORY_ROOMS, DormitoryUserController::getDormitoryRooms);
        requestHandler.addRequestHandler(Code.RequestCode.SELECT_PRIORITY_APPLICATION, DormitoryUserController::selectPriorityApplication);
        requestHandler.addRequestHandler(Code.RequestCode.APPLY_ROOMMATE, DormitoryUserController::applyRoommate);
        requestHandler.addRequestHandler(Code.RequestCode.APPLY_MEAL, DormitoryUserController::applyMeal);
        requestHandler.addRequestHandler(Code.RequestCode.APPLY_ROOM, DormitoryUserController::applyRoom);
        requestHandler.addRequestHandler(Code.RequestCode.GET_SELECTION_RESULT, DormitoryUserController::getSelectionResult);
        requestHandler.addRequestHandler(Code.RequestCode.GET_MEAL_PLAN, DormitoryUserController::getMeritAndDemeritPoints);
        requestHandler.addRequestHandler(Code.RequestCode.GET_FILE_FOR_PROOF, DormitoryUserController::getFileForProof);
        requestHandler.addRequestHandler(Code.RequestCode.UPLOAD_FILE_FOR_PROOF,DormitoryUserController::uploadFileForProof);
        requestHandler.addRequestHandler(Code.RequestCode.MOVE_OUT,DormitoryUserController::moveOut);
        requestHandler.addRequestHandler(Code.RequestCode.UPLOAD_TUBER_REPORT,DormitoryUserController::uploadTuberReport);
        requestHandler.addRequestHandler(Code.RequestCode.APPLICATION,DormitoryUserController::application);//DormitoryUserController

        requestHandler.addRequestHandler(Code.RequestCode.REGISTER_SELECTION_INFO, DormitoryAdminController::registerSelectionInfo);
        requestHandler.addRequestHandler(Code.RequestCode.GET_APPLICANTS,DormitoryAdminController::getApplicant);
        requestHandler.addRequestHandler(Code.RequestCode.SELECT_APPLICANTS,DormitoryAdminController::selectApplicants);
        requestHandler.addRequestHandler(Code.RequestCode.MANAGEMENT_MERIT_POINT,DormitoryAdminController::managementMeritPoint);
        requestHandler.addRequestHandler(Code.RequestCode.GET_MOVE_OUT_APPLICANTS,DormitoryAdminController::getMoveOutApplicants);
        requestHandler.addRequestHandler(Code.RequestCode.APPROVE_MOVE_OUT,DormitoryAdminController::approveMoveOut);//DormitoryAdminController

        requestHandler.addRequestHandler(Code.RequestCode.GET_PAYMENT_AMOUNT, PaymentController::getPaymentAmount);
        requestHandler.addRequestHandler(Code.RequestCode.GET_PAYMENT_STATUS, PaymentController::getPaymentStatus);
        requestHandler.addRequestHandler(Code.RequestCode.BANK_TRANSFER, PaymentController::payByBankTransfer);
        requestHandler.addRequestHandler(Code.RequestCode.CARD_MOVEMENT, PaymentController::payByCard);
        requestHandler.addRequestHandler(Code.RequestCode.REFUND_REQUEST, PaymentController::requestRefund);
//  //    requestHandler.addRequestHandler(Code.RequestCode.REFUND_CONFIRM, PaymentController::confirmRefund);
        requestHandler.addRequestHandler(Code.RequestCode.GET_REFUND_STATEMENT, PaymentController::getRefundStatus);//paymentController


    }
}
