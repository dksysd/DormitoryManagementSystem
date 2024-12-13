package server.controller;


import server.persistence.dao.*;
import server.persistence.dto.*;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static server.util.ProtocolValidator.*;

public class DormitoryAdminController {

    /**
     * @param protocol header(type:request, dataType: TLV, code: REGISTER_SELECTION_INFO, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: SELECTION_INFO, dataLength:,)
     *                 data: 우선선발 )
     *                 2 ( header(type: value, dataType: string, code: SELECTION_SCHEDULE, dataLength:,)
     *                 data: 시작일 ),
     *                 3 ( header(type: value, dataType: string, code: SELECTION_SCHEDULE, dataLength:,)
     *                 data: 종료일 )
     *                 4 ( header(type: value, dataType: string, code: SESSIONID, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
     * data: null
     */
    public static Protocol<?> registerSelectionInfo(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String title = (String) protocol.getChildren().getFirst().getData();
        String day = (String) protocol.getChildren().get(1).getData();
        String finish = (String) protocol.getChildren().get(2).getData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            SelectionScheduleDTO dto = SelectionScheduleDTO.builder()
                    .startedAt(LocalDateTime.parse(day, formatter))
                    .endedAt(LocalDateTime.parse(finish, formatter))
                    .title(title)
                    .createdAt(LocalDateTime.now())
                    .build();
            dao.save(dto);

            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 신청자 내역을 가져와서 프로토콜에 실어 보내는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_applicant, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : get_applicant, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getApplicant(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        UserDAO dao = new UserDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            List<String> list = dao.findAllOfSelection();
            for (String s : list) {
                Protocol<String> child = new Protocol<>(new Header(Type.VALUE, DataType.STRING, Code.ValueCode.ID, 0), s);
                result.addChild(child);
            }
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setDataType(DataType.TLV);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);

        return result;
    }

    /**
     * 선발자 학번을 받아와서 결정하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: select_applicant, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: raw, code: proof_image, dataLength:,)
     *                 data: 이미지 )
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : OK, dataLength :, ))
     * @return (에러의 경우) header(type : Error, dataType : TLV, code : Error dataLength: 0)
     * 정상 -> header(type : Response, dataType : TLV, code : OK dataLength: 0)
     * data: null
     */


    public static Protocol<?> selectApplicants(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            dao.updateSelectionApplication(id, "선발완료");
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 상벌점 내역을 추가하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: manage_merit, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: raw, code: proof_image, dataLength:,)
     *                 data: 이미지 )
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : OK dataLength : 0, ))
     * @return (에러의 경우) header(type : Error, dataType : TLV, code : Error dataLength: 0)
     * 정상 -> header(type : Response, dataType : TLV, code : OK dataLength: 0)
     * data: null
     */

    public static Protocol<?> managementMeritPoint(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        DemeritPointDAO dao = new DemeritPointDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            dao.savePoint((String) protocol.getChildren().getFirst().getData()
                    , (String) protocol.getChildren().get(1).getData(),
                    (Integer) protocol.getChildren().get(2).getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }


    public static Protocol<?> getMoveOutApplicants(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        MoveOutRequestDAO dao = new MoveOutRequestDAO();
        List<String> list;
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            list = dao.findAllOfMoveOut();
            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setCode(Code.ValueCode.ID);
                childHeader.setDataType(DataType.STRING);
                child.setHeader(childHeader);
                result.addChild(child);
            }

            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
            resultHeader.setCode(Code.ResponseCode.OK);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 퇴사절차를 진행한 뒤, 환불까지 처리하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: approve_moveout, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: Id, dataLength:,)
     *                 data: 학생 uid )
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK ( 틀리면 에러) dataLength: 0)
     * data: null
     */
    public static Protocol<?> approveMoveOut(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        MoveOutRequestDAO MORDao = new MoveOutRequestDAO();
        PaymentRefundDAO PRDao = new PaymentRefundDAO();
        SelectionApplicationDAO selectionApplicationDAO = new SelectionApplicationDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        PaymentRefundDTO PRDto = PRDao.findByUid(id);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            MORDao.findByUid(id).getMoveOutRequestStatusDTO().setStatusName("퇴사완료");
            PRDto.getPaymentDTO().getPaymentStatusDTO().setStatusName("환불완료");
            UserDTO userDTO = selectionApplicationDAO.findByUid(getIdBySessionId(id)).getRoommateUserDTO();
            SelectionApplicationDTO roommateDTO = selectionApplicationDAO.findByUid(userDTO.getUid());
            roommateDTO.setRoommateUserDTO(null);
            selectionApplicationDAO.update(roommateDTO);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }
}