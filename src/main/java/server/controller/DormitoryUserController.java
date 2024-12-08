package server.controller;

import server.persistence.dao.*;
import server.persistence.dto.SelectionApplicationDTO;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.util.List;

import static server.util.ProtocolValidator.*;

public class DormitoryUserController {
    public static Protocol<?> getSelectionSchedule(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = result.getHeader();
        String id = (String) protocol.getChildren().getFirst().getData();

        if (verifySessionId(id)) {
            List<String> list = dao.findAllTitleIntoString();

            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = child.getHeader();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.SELECTION_SCHEDULE);
                child.setData(s);

                result.addChild(child);
            }

            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        return result;
    }

    public static Protocol<?> getMealPlan(Protocol<?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        MealPlanDAO dao = new MealPlanDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();

        if (verifySessionId(id)) {
            List<String> list = dao.findAllMealTypeIntoString();

            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.MEAL_PLAN);
                child.setData(s);
                child.setHeader(childHeader);

                result.addChild(child);
            }

            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> getDormitoryRooms(Protocol <?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        RoomTypeDAO dao = new RoomTypeDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();

        if (verifySessionId(id)) {
            List<String> list = dao.findAllIntoString();

            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = child.getHeader();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.DORMITORY_ROOM_TYPE);
                child.setData(s);

                result.addChild(child);
            }

            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> selectPriorityApplication(Protocol<?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getFirst().getData();
        String id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
        Integer preference = (Integer) protocol.getChildren().get(1).getData();

        if (verifySessionId(sessionId)) {
            dao.updatePreference(id, preference);
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> applyRoommate(Protocol<?> protocol) throws SQLException {
        Header header = new Header();
        UserDAO dao = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        if (verifySessionId(id)) {
            dao.updateRoommate(getIdByIdBySessionId(id), (Integer) protocol.getChildren().get(1).getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);

        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> applyMeal(Protocol<?> protocol) throws SQLException {
        Header header = new Header();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        if (verifySessionId(id)) {
            dao.updateMealPlan(id, (String) protocol.getChildren().get(1).getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> applyRoom(Protocol<?> protocol) throws SQLException {
        Header header = new Header();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        if (verifySessionId(id)) {
            dao.updateRoomType(id,(String) protocol.getChildren().get(1).getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> getSelectionResult(Protocol<?> protocol) throws SQLException {
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        String uid = getIdByIdBySessionId(id);
        if (verifySessionId(id)) {
            String statusName = dao.findByUid(uid).getSelectionApplicationStatusDTO().getStatusName();
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);

            Protocol<String> child = new Protocol<>();
            Header childHeader = new Header();
            childHeader.setType(Type.VALUE);
            childHeader.setDataType(DataType.STRING);
            childHeader.setCode(Code.ResponseCode.ValueCode.SELECTION_STATUS);
            child.setHeader(childHeader);
            child.setData(statusName);
            result.addChild(child);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> getMeritAndDemeritPoints(Protocol<?> protocol) throws SQLException {
        DemeritPointDAO dao = new DemeritPointDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        if (verifySessionId(id)) {
            List<Integer> list = dao.findAllPointIntoInt();

            // FIXME : list? SUM?
            for (Integer i : list) {
                Protocol<Integer> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.INTEGER);
                child.setHeader(childHeader);
                child.setData(i);
                result.addChild(child);
            }

            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    public static Protocol<?> getFileForProof (Protocol<?> protocol) throws SQLException {
        UserDAO dao = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getFirst().getData();
        if (verifySessionId(id)) {
            //TODO : File 어케 받아와요?
        }

        return result;
    }
}
