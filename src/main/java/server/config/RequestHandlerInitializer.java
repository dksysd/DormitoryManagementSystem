package server.config;

import server.controller.*;
import server.core.handler.RequestHandler;
import shared.protocol.persistence.*;

/**
 * RequestHandlerInitializer 인터페이스는 {@link RequestHandler}를 초기화하기 위한 유틸리티를 제공합니다.
 * 다양한 요청 코드와 해당 요청을 처리할 컨트롤러 메서드를 매핑하여, 서버에서 수신된 요청을 적절히 처리할 수 있도록 합니다.
 */
public interface RequestHandlerInitializer {

    /**
     * {@link RequestHandler}를 초기화합니다.
     * 이전에 등록된 모든 요청 핸들러를 제거하고, 새롭게 정의된 요청 코드와 처리 메서드의 매핑을 추가합니다.
     *
     * 요청 코드와 처리기 메서드는 다음과 같은 카테고리와 컨트롤러로 구성되어 있습니다:
     * - 인증 및 세션 관리 요청: {@link AuthController}
     * - 사용자 정보 관리 요청: {@link UserController}
     * - 기숙사 사용자 요청: {@link DormitoryUserController}
     * - 기숙사 관리자 요청: {@link DormitoryAdminController}
     * - 결제 관련 요청: {@link PaymentController}
     *
     * @param requestHandler 초기화할 {@link RequestHandler} 객체
     */
    static void init(RequestHandler requestHandler) {
        // 기존 핸들러를 제거합니다.
        requestHandler.clearRequestHandlers();

        // AuthController 처리기 등록
        requestHandler.addRequestHandler(Code.RequestCode.LOGIN, AuthController::login);
        requestHandler.addRequestHandler(Code.RequestCode.LOGOUT, AuthController::logout);
        requestHandler.addRequestHandler(Code.RequestCode.REFRESH_SESSION, AuthController::refreshSession);

        // UserController 처리기 등록
        requestHandler.addRequestHandler(Code.RequestCode.GET_USER_INFO, UserController::getUserInfo);

        // DormitoryUserController 처리기 등록
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
        requestHandler.addRequestHandler(Code.RequestCode.UPLOAD_FILE_FOR_PROOF, DormitoryUserController::uploadFileForProof);
        requestHandler.addRequestHandler(Code.RequestCode.MOVE_OUT, DormitoryUserController::moveOut);
        requestHandler.addRequestHandler(Code.RequestCode.UPLOAD_TUBER_REPORT, DormitoryUserController::uploadTuberReport);
        requestHandler.addRequestHandler(Code.RequestCode.APPLICATION, DormitoryUserController::application);

        // DormitoryAdminController 처리기 등록
        requestHandler.addRequestHandler(Code.RequestCode.REGISTER_SELECTION_INFO, DormitoryAdminController::registerSelectionInfo);
        requestHandler.addRequestHandler(Code.RequestCode.GET_APPLICANTS, DormitoryAdminController::getApplicant);
        requestHandler.addRequestHandler(Code.RequestCode.SELECT_APPLICANTS, DormitoryAdminController::selectApplicants);
        requestHandler.addRequestHandler(Code.RequestCode.MANAGEMENT_MERIT_POINT, DormitoryAdminController::managementMeritPoint);
        requestHandler.addRequestHandler(Code.RequestCode.GET_MOVE_OUT_APPLICANTS, DormitoryAdminController::getMoveOutApplicants);
        requestHandler.addRequestHandler(Code.RequestCode.APPROVE_MOVE_OUT, DormitoryAdminController::approveMoveOut);

        // PaymentController 처리기 등록
        requestHandler.addRequestHandler(Code.RequestCode.GET_PAYMENT_AMOUNT, PaymentController::getPaymentAmount);
        requestHandler.addRequestHandler(Code.RequestCode.GET_PAYMENT_STATUS, PaymentController::getPaymentStatus);
        requestHandler.addRequestHandler(Code.RequestCode.BANK_TRANSFER, PaymentController::payByBankTransfer);
        requestHandler.addRequestHandler(Code.RequestCode.CARD_MOVEMENT, PaymentController::payByCard);
        requestHandler.addRequestHandler(Code.RequestCode.REFUND_REQUEST, PaymentController::requestRefund);
        requestHandler.addRequestHandler(Code.RequestCode.GET_REFUND_STATEMENT, PaymentController::getRefundStatus);
    }
}
