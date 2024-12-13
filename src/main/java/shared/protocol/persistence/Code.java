package shared.protocol.persistence;

/**
 * Code 인터페이스는 프로토콜 통신에서 사용하는 코드 값을 정의하기 위한 공통 인터페이스입니다.
 * <p>
 * 이 인터페이스는 다양한 유형의 코드(`RequestCode`, `ResponseCode`, `ErrorCode`, `ValueCode`)를 지원하며,
 * 각각의 코드는 통신 프로토콜의 요청, 응답, 오류, 값과 관련된 작업을 나타냅니다.
 *
 * <p>하위 코드 유형:
 * <ul>
 *     <li>{@link RequestCode}: 클라이언트 요청에서 사용되는 코드 값.</li>
 *     <li>{@link ResponseCode}: 서버 응답에서 사용되는 코드 값.</li>
 *     <li>{@link ErrorCode}: 통신 중 발생한 오류를 나타내는 코드 값.</li>
 *     <li>{@link ValueCode}: 프로토콜 내 특정 데이터 키를 나타내는 코드 값.</li>
 * </ul>
 */
public interface Code extends HeaderElement {
    /**
     * RequestCode 열거형은 클라이언트 요청에 사용되는 다양한 작업 코드를 정의합니다.
     * 각 값은 특정 요청을 나타내며, 서버에서 요청을 처리할 때 해당 코드로 작업의 종류를 인식합니다.
     */
    enum RequestCode implements Code {

        /**
         * 사용자가 로그인 요청을 보낼 때 사용하는 코드.
         */
        LOGIN,

        /**
         * 사용자가 로그아웃 요청을 보낼 때 사용하는 코드.
         */
        LOGOUT,

        /**
         * 세션을 갱신하거나 연장할 때 사용하는 코드.
         */
        REFRESH_SESSION,

        /**
         * 사용자 정보를 요청할 때 사용하는 코드.
         */
        GET_USER_INFO,

        /**
         * 선택 일정을 요청할 때 사용하는 코드.
         */
        GET_SELECTION_SCHEDULE,

        /**
         * 급식 계획을 요청할 때 사용하는 코드.
         */
        GET_MEAL_PLAN,

        /**
         * 기숙사 방 목록을 요청할 때 사용하는 코드.
         */
        GET_DORMITORY_ROOMS,

        /**
         * 기숙사 방 유형을 요청할 때 사용하는 코드.
         */
        GET_DORMITORY_ROOM_TYPE,

        /**
         * 우선순위 신청에 사용할 데이터 요청 시 사용하는 코드.
         */
        SELECT_PRIORITY_APPLICATION,

        /**
         * 룸메이트 신청을 보낼 때 사용하는 코드.
         */
        APPLY_ROOMMATE,

        /**
         * 급식 신청을 보낼 때 사용하는 코드.
         */
        APPLY_MEAL,

        /**
         * 방 신청을 보낼 때 사용하는 코드.
         */
        APPLY_ROOM,

        /**
         * 퇴실 요청을 보낼 때 사용하는 코드.
         */
        MOVE_OUT,

        /**
         * 선택 결과를 요청할 때 사용하는 코드.
         */
        GET_SELECTION_RESULT,

        /**
         * 상벌점(메리트 및 디메리트 점수)을 요청할 때 사용하는 코드.
         */
        GET_MERIT_AND_DEMERIT_POINTS,

        /**
         * 튜버 보고서를 업로드할 때 사용하는 코드.
         */
        UPLOAD_TUBER_REPORT,

        /**
         * 증명용 파일을 요청할 때 사용하는 코드.
         */
        GET_FILE_FOR_PROOF,

        /**
         * 증명용 파일을 업로드할 때 사용하는 코드.
         */
        UPLOAD_FILE_FOR_PROOF,

        /**
         * 학생 관련 신청 데이터를 처리할 때 사용하는 코드.
         */
        APPLICATION,

        /**
         * 선택 정보를 등록할 때 관리자 권한으로 사용하는 코드.
         */
        REGISTER_SELECTION_INFO,

        /**
         * 신청 목록을 요청할 때 관리자 권한으로 사용하는 코드.
         */
        GET_APPLICANTS,

        /**
         * 신청자를 선택할 때 관리자 권한으로 사용하는 코드.
         */
        SELECT_APPLICANTS,

        /**
         * 상벌점 관리에 사용되는 관리자 요청 코드.
         */
        MANAGEMENT_MERIT_POINT,

        /**
         * 튜버 보고서를 확인할 때 관리자 권한으로 사용하는 코드.
         */
        CONFIRM_TUBER_REPORT,

        /**
         * 퇴실 신청자 목록을 요청할 때 관리자 권한으로 사용하는 코드.
         */
        GET_MOVE_OUT_APPLICANTS,

        /**
         * 파일 증명을 확인할 때 사용하는 관리자 권한 코드.
         */
        CONFIRM_FILE_PROOF,

        /**
         * 퇴실 요청을 승인할 때 사용하는 관리자 권한 코드.
         */
        APPROVE_MOVE_OUT,

        /**
         * 결제 금액 정보를 요청할 때 사용하는 코드.
         */
        GET_PAYMENT_AMOUNT,

        /**
         * 결제 상태를 요청할 때 사용하는 코드.
         */
        GET_PAYMENT_STATUS,

        /**
         * 은행 송금 요청을 보낼 때 사용하는 코드.
         */
        BANK_TRANSFER,

        /**
         * 카드 결제 요청을 보낼 때 사용하는 코드.
         */
        CARD_MOVEMENT,

        /**
         * 환불 요청 데이터를 서버로 보낼 때 사용하는 코드.
         */
        REFUND_REQUEST,

        /**
         * 환불 내역을 요청할 때 사용하는 코드.
         */
        GET_REFUND_STATEMENT,

        /**
         * 퇴실 상태를 검증할 때 사용하는 코드.
         */
        CHECK_MOVE_OUT,

        /**
         * 청구 내역을 요청할 때 사용하는 코드.
         */
        BILL,

        /**
         * 결제 영수증을 요청할 때 사용하는 코드.
         */
        GET_RECEIPT,

        /**
         * 결제 검증 요청에 사용하는 코드.
         */
        GET_PAYMENT_CHECK
    }

    /**
     * ResponseCode 열거형은 서버 응답에 사용되는 다양한 응답 코드를 정의합니다.
     * 각 값은 응답 메시지에서 상태를 의미하는 식별자로 사용됩니다.
     */
    enum ResponseCode implements Code {
        /**
         * 요청을 성공적으로 처리한 경우에 사용하는 코드.
         */
        OK,
    }

    /**
     * ErrorCode 열거형은 서버 및 클라이언트 간의 통신 중 발생할 수 있는 다양한 오류 코드를 정의합니다.
     * 각 값은 특정 오류 상황에 대해 상세한 정보를 제공합니다.
     */
    enum ErrorCode implements Code {

        /** 서버 내부에서 처리 도중 예기치 않은 문제가 발생했음을 나타내는 코드. */
        INTERNAL_SERVER_ERROR,

        /** 요청에서 잘못된 값이 전달되었음을 나타내는 코드. */
        INVALID_VALUE,

        /** 요청에서 예상하지 않은 데이터 유형이 전달되었음을 나타내는 코드. */
        INVALID_TYPE,

        /** 통신 프로토콜에서 잘못된 코드 값이 사용되었음을 나타내는 코드. */
        INVALID_CODE,

        /** 프로토콜 형식이 올바르지 않거나 처리할 수 없는 상황을 나타내는 코드. */
        INVALID_PROTOCOL,

        /** 클라이언트의 요청이 권한을 필요로 하지만, 적절한 권한이 없음을 나타내는 코드. */
        UNAUTHORIZED,

        /** 클라이언트의 요청 형식이 잘못되었거나, 처리할 수 없는 요청임을 나타내는 코드. */
        INVALID_REQUEST
    }

    /**
     * ValueCode 열거형은 프로토콜 내 데이터 키에 해당하는 다양한 값을 정의합니다.
     * 각 값은 요청 또는 응답 메시지에서 특정 데이터를 의미하는 식별자로 사용됩니다.
     */
    enum ValueCode implements Code {

        /** 사용자 ID를 나타내는 값. */
        ID,

        /** 사용자 비밀번호를 나타내는 값. */
        PASSWORD,

        /** 사용자 이름을 나타내는 값. */
        USER_NAME,

        /** 사용자 전화번호를 나타내는 값. */
        PHONE_NUMBER,

        /** 사용자의 성별 이름(예: 남성, 여성)을 나타내는 값. */
        GENDER_NAME,

        /** 사용자의 행정 구역(도)을 나타내는 값. */
        DO,

        /** 사용자의 행정 구역(시)을 나타내는 값. */
        SI,

        /** 사용자의 상세 주소를 나타내는 값. */
        DETAIL_ADDRESS,

        /** 세션 식별자를 나타내는 값. */
        SESSION_ID,

        /** 선택 관련 정보를 나타내는 값. */
        SELECTION_INFO,

        /** 선택의 상태를 나타내는 값. */
        SELECTION_STATUS,

        /** 사용자의 총 메리트(상점) 점수를 나타내는 값. */
        SUM_OF_MERIT_POINTS,

        /** 가격(총액 혹은 특정 항목의 비용)을 나타내는 값. */
        PRICE,

        /** 증명 파일을 나타내는 값. */
        PROOF_FILE,

        /** 사용자의 선호도를 나타내는 값. */
        PREFERENCE,

        /** 사용자의 유형(예: 학생, 관리자)을 나타내는 값. */
        USER_TYPE,

        /** 튜버 보고서를 나타내는 값. */
        TUBER_REPORT,

        /** 결제 상태(예: 완료, 대기 중)를 나타내는 값. */
        PAYMENT_STATUS_NAME,

        /** 결제 금액을 나타내는 값. */
        PAYMENT_AMOUNT,

        /** 은행 계좌 번호를 나타내는 값. */
        ACCOUNT_NUMBER,

        /** 계좌 소유자의 이름을 나타내는 값. */
        ACCOUNT_HOLDER_NAME,

        /** 은행 이름을 나타내는 값. */
        BANK_NAME,

        /** 신용카드 또는 계좌 관리 번호를 나타내는 값. */
        CARE_NUMBER,

        /** 카드 발급사를 나타내는 값. */
        CARD_ISSUER,

        /** 환불 상태(예: 승인됨, 대기 중)를 나타내는 값. */
        REFUND_STATUS,

        /** 환불 금액을 나타내는 값. */
        REFUND_AMOUNT,

        /** 카드 코드를 나타내는 값. */
        CARD_CODE,

        /** 은행 코드를 나타내는 값. */
        BANK_CODE,

        /** 환불 사유를 나타내는 값. */
        REFUND_REASON,

        /** 우선순위 선택을 나타내는 값. */
        PRIORITY,

        /** 급식 계획을 나타내는 값. */
        MEAL_PLAN,

        /** 급식 유형을 나타내는 값(예: 조식, 중식, 석식). */
        MEAL_TYPE,

        /** 퇴실 상태를 나타내는 값. */
        MOVE_OUT_STATUS,

        /** 기숙사 방 유형을 나타내는 값. */
        DORMITORY_ROOM_TYPE,

        /** 퇴실 요청 정보를 나타내는 값. */
        MOVE_OUT_REQUEST,

        /** 선택 일정 정보를 나타내는 값. */
        SELECTION_SCHEDULE,

        /** 디메리트(벌점) 점수를 나타내는 값. */
        DEMERIT_POINT,

        /** 일반 메시지(정보 또는 오류)를 나타내는 값. */
        MESSAGE,

        /** 디메리트 사유를 나타내는 값. */
        DEMERIT_REASON,

        /** 침대 번호를 나타내는 값. */
        BED_NUMBER,

        /** 기숙사 방 번호를 나타내는 값. */
        ROOM_NUMBER,

        /** 코골이에 대한 Boolean 상태를 나타내는 값(true/false). */
        SNORE,

        /** 1년간 동일한 방 유지 여부를 나타내는 Boolean 값. */
        ONEYEAR_LASTING
    }
}
