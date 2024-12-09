package server.controller;


import server.persistence.dao.UserDAO;
import server.persistence.dto.UserDTO;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import static server.util.ProtocolValidator.getIdBySessionId;


public class UserController {
    /**
     * @param protocol header(type:request, dataType: TLV, code: GET_USER_INFO, dataLength:)
     *                 data:
     *                 children<
     *                 header(type: value, dataType: string, code: SESSION_ID, dataLength:)
     *                 data: sessionId
     *                 >
     * @return protocol header(type:response, dataType: TLV, code: OK, dataLength:)
     * data:
     * children<
     * 1 header(type: value, dataType: string, code: ID, dataLength:)
     * data: 아이디
     * 2 header(type: value, dataType: string, code: USER_NAME, dataLength:)
     * data: 이름
     * 3 header(type: value, dataType: string, code: PHONE_NUMBER, dataLength:)
     * data: 전화번호
     * 4 header(type: value, dataType: string, code: GENDER_NAME, dataLength:)
     * data: 성별
     * 5 header(type: value, dataType: string, code: DO, dataLength:)
     * data: 도(주소)
     * 6 header(type: value, dataType: string, code: SI, dataLength:)
     * data: 시(주소)
     * 7 header(type: value, dataType: string, code: DETAIL_ADDRESS, dataLength:)
     * data: 상세주소
     * >
     */
    public static Protocol<?> getUserInfo(Protocol<?> protocol) throws SQLException {
        Header header = new Header();
        Protocol<?> resProtocol = new Protocol<>();
        UserDAO userDAO = new UserDAO();
        try {
            String id = getIdBySessionId((String) protocol.getChildren().getFirst().getData());

            if (id == null) {
                header.setType(Type.RESPONSE);
                header.setDataType(DataType.TLV);
                header.setCode(Code.ErrorCode.UNAUTHORIZED);
                resProtocol.setHeader(header);
                return resProtocol;
            }

            UserDTO userDTO = userDAO.findByUid(id);
            if (userDTO == null) {
                header.setType(Type.RESPONSE);
                header.setDataType(DataType.TLV);
                header.setCode(Code.ErrorCode.UNAUTHORIZED);
                resProtocol.setHeader(header);
                return resProtocol;
            }

            header.setType(Type.RESPONSE);
            header.setDataType(DataType.TLV);
            header.setCode(Code.ResponseCode.OK);
            resProtocol.setHeader(header);

            addChildToProtocol(resProtocol, Code.ValueCode.USER_NAME, userDTO.getUserName());
            addChildToProtocol(resProtocol, Code.ValueCode.PHONE_NUMBER, userDTO.getPhoneNumber());
            addChildToProtocol(resProtocol, Code.ValueCode.GENDER_NAME, userDTO.getGenderCodeDTO().getCodeName());
            addChildToProtocol(resProtocol, Code.ValueCode.DO, userDTO.getAddressDTO().get_do());
            addChildToProtocol(resProtocol, Code.ValueCode.SI, userDTO.getAddressDTO().getSi());
            addChildToProtocol(resProtocol, Code.ValueCode.DETAIL_ADDRESS, userDTO.getAddressDTO().getDetailAddress());

        } catch (Exception e) {
            header.setCode(Code.ErrorCode.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
        resProtocol.setHeader(header);
        return resProtocol;
    }

    private static void addChildToProtocol(Protocol<?> protocol, Code.ValueCode code, String data) {
        protocol.addChild(new Protocol<>(new Header(Type.VALUE, DataType.STRING, code, 0), data));
    }



}