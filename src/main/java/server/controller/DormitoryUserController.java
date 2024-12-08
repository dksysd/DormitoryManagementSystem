package server.controller;

import server.persistence.dao.DormitoryRoomTypeDAO;
import server.persistence.dao.SelectionScheduleDAO;
import shared.protocol.persistence.*;
import server.persistence.dao.MealPlanDAO;

import java.sql.SQLException;
import java.util.List;

import static server.util.ProtocolValidator.*;

public class DormitoryUserController {
    public static Protocol<?> getSelectionSchedule(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = result.getHeader();
        String id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());

        if (verifySessionId(id)) {
            // TODO : List<String> findAll 기능
            List<String> list = dao.findAllIntoString();

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
        Header resultHeader = result.getHeader();
        String id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());

        if (verifySessionId(id)) {
            // TODO : List<String> findALL 기능
            List<String> list = dao.findAllIntoString();

            for (String s : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = child.getHeader();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.MEAL_PLAN);
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

    public static Protocol<?> getDormitoryRooms(Protocol <?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        DormitoryRoomTypeDAO dao = new DormitoryRoomTypeDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = result.getHeader();
        String id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());

        if (verifySessionId(id)) {
            // TODO : List<String> findALL 기능
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

        return result;
    }


}
