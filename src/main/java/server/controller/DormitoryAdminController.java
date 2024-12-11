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


    public static Protocol<?> registerSelectionInfo(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        String title = (String) protocol.getChildren().getFirst().getData();
        String day = (String) protocol.getChildren().get(1).getData();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            SelectionScheduleDTO dto = SelectionScheduleDTO.builder()
                    .startedAt(LocalDateTime.parse(day, formatter))
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

    public static Protocol<?> getApplicant(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        UserDAO dao = new UserDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            List<String> list = dao.findAllOfSelection();

            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setCode(Code.ValueCode.ID);
                childHeader.setDataType(DataType.STRING);
                child.setHeader(childHeader);

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

    public static Protocol<?> selectApplicants(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        if (verifySessionId(sessionId) && isAdmin(sessionId)) {
            dao.updateSelectionApplication(id, (String) protocol.getChildren().getFirst().getData());
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
     * @param protocol header(type:request, dataType: TLV, code: APPROVE_MOVEOUT, dataLength:)
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
        Header resultHeader = new Header(Type.RESPONSE,DataType.TLV,Code.ResponseCode.OK,0);
        MoveOutRequestDAO MORDao = new MoveOutRequestDAO();
        PaymentRefundDAO PRDao = new PaymentRefundDAO();
        SelectionApplicationDAO selectionApplicationDAO= new SelectionApplicationDAO();
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
        }else{
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }
}