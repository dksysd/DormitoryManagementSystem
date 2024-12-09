package server.controller;

import server.core.SessionManager;
import server.persistence.dao.UserDAO;
import server.persistence.dto.UserDTO;
import shared.protocol.persistence.*;
import java.sql.SQLException;
import java.util.Objects;

import static server.util.ProtocolValidator.verifySessionId;

//refreshSession 미완
public class AuthController implements Controller {

    /**
     * id : 숫자 8자리 제한, password: 8~20자리, 영어, 특수문자, 숫자 포함
     *
     * @param protocol header(type:request, dataType: TLV, code: LOGIN, dataLength:)
     *                 data:
     *                 children<
     *                 1 header(type: value, dataType: string, code: id, dataLength:)
     *                 data:아이디.
     *                 2 header(type: value, dataType: string, code: password, dataLength:)
     *                 data: 비밀번호
     *                 >
     * @return 성공 header(type: response, dataType: TLV, code: OK, dataLength:
     * children<
     *          1 header(type:value, dataType: String, code: SessionId, dataLength:)
     *          data: 세션아이디,
     *          2 header(type:value, dataType: String, code: USER_TYPE_ID, dataLength:)
     *          data: 사용자 유형 아이디(관리자 or 학생)
     *          >
     * 실패 header(type: response, dataType: TLV, code: 에러코드(개중 보고 에러원인 판단), dataLength: 0)
     * data:
     *
     */
    public static Protocol<?> login(Protocol<?> protocol) throws SQLException {

        Protocol<?> resProtocol = new Protocol<>();
        Protocol<String> childProtocol1 = new Protocol<>();
        Protocol<String> childProtocol2 = new Protocol<>();
        Header header = new Header(Type.RESPONSE,DataType.TLV,Code.ResponseCode.OK,0);


        String sessionId = "";
        try {
            String id = (String) protocol.getChildren().get(0).getData();
            String pw = (String) protocol.getChildren().get(1).getData();
            UserDAO userDAO = new UserDAO();
            UserDTO userDTO = userDAO.findByUid(id);

            if (isValidLoginCredentials(id, pw, header)) {
                // 세션아이디 발급(세션 저장소에 id,pw 추가)
                childProtocol1.setHeader(new Header(Type.VALUE,DataType.STRING,Code.ValueCode.SESSION_ID,0));
                childProtocol2.setHeader(new Header(Type.VALUE,DataType.STRING,Code.ValueCode.USER_TYPE_ID,0));
                childProtocol1.setData(sessionId);
                childProtocol2.setData(userDTO.getUserTypeDTO().getTypeName());
                resProtocol.addChild(childProtocol1);
                resProtocol.addChild(childProtocol2);
            }
        } catch (SQLException e) {
            header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
        }
        resProtocol.setHeader(header);
        return resProtocol;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: LOGOUT, dataLength:)
     *                 data:
     *                 children<
     *                 1 header(type: value, dataType: string, code: SessionId, dataLength:)
     *                 data: sessionId
     *                 >
     * @return header(type : response, dataType : TLV, code : OK ( 실패시 에러코드), dataLength :
     * data:
     */
    public static Protocol<?> logout(Protocol<?> protocol) {
        Protocol<?> resProtocol = new Protocol<>();
        Header header = new Header();
        String sessionId;
        try {
            sessionId = (String) protocol.getChildren().getFirst().getData();
            header.setType(Type.RESPONSE);
            header.setDataType(DataType.TLV);
            if (verifySessionId(sessionId)) {//세션아이디 검증 및 처리
                header.setCode(Code.ResponseCode.OK);
            } else header.setCode(Code.ErrorCode.UNAUTHORIZED);
            resProtocol.setHeader(header);
        } catch (Exception e) {
            header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return resProtocol;
    }
    /**
     * @param protocol header(type:request, dataType: TLV, code: REFRESH_SESSION, dataLength:)
     *                 data:
     *                 children<
     *                 1 header(type: value, dataType: string, code: SessionId, dataLength:)
     *                 data: sessionId
     *                 >
     * @return header(type : response, dataType : TLV, code : OK ( 실패시 에러코드), dataLength :
     *                 data:
     **/
    public static Protocol<?> refreshSession(Protocol<?> protocol) {
        Protocol<?> resProtocol = new Protocol<>();
        Header header = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        try {
            String sessionId = (String) protocol.getChildren().getFirst().getData();

            if (verifySessionId(sessionId)) {
                SessionManager sessionManager = SessionManager.getINSTANCE();
                sessionManager.getSession(sessionId).touch();
            } else {
                header.setCode(Code.ResponseCode.ErrorCode.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // 예외 처리 (예상치 못한 오류)
            header.setCode(Code.ResponseCode.ErrorCode.INTERNAL_SERVER_ERROR);
        }
        resProtocol.setHeader(header);
        return resProtocol;
    }

    private static boolean isValidLoginCredentials(String id, String password, Header header) throws SQLException {
        try {
            // ID 형식 검증 (8자리 숫자)
            if (!isValidId(id) && !isValidPassword(password)) {
                // 실제 데이터베이스 검증
                if (authenticateUser(id, password)) {
                    return true;
                }
                header.setCode(Code.ErrorCode.UNAUTHORIZED);
            }
            header.setCode(Code.ErrorCode.INVALID_VALUE);
        } catch (SQLException e) {
            header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return false;
    }

    private static boolean isValidId(String id) {
        return id != null && id.matches("\\d{8}");
    }

    private static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }

        int validationCount = 0;

        if (password.matches(".*[a-zA-Z].*")) validationCount++; // 영문자 포함
        if (password.matches(".*\\d.*")) validationCount++;      // 숫자 포함
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) validationCount++; // 특수문자 포함

        return validationCount >= 3;
    }

    private static boolean authenticateUser(String id, String password) throws SQLException {
        UserDAO userDAO = new UserDAO();
        UserDTO userDTO = userDAO.findByUid(id);

        return userDTO != null && Objects.equals(password, userDTO.getLoginPassword());
    }

}