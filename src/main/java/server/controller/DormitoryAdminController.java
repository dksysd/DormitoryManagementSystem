package server.controller;


import server.persistence.dao.*;
import server.persistence.dto.SelectionScheduleDTO;
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

        if (verifySessionId(sessionId)) {
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
        if (verifySessionId(sessionId)) {
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
        if (verifySessionId(sessionId)) {
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
        if (verifySessionId(sessionId)) {
            dao.savePoint((String) protocol.getChildren().getFirst().getData()
                    ,(String) protocol.getChildren().get(1).getData(),
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
        if (verifySessionId(sessionId)) {
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

    public static Protocol<?> approveMoveOut(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();


        result.setHeader(resultHeader);
        return result;
    }
}