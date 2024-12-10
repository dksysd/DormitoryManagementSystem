package server.controller;

import server.persistence.dao.*;
import server.persistence.dto.SelectionApplicationDTO;
import server.persistence.dto.SelectionDTO;
import server.persistence.dto.UserDTO;
import server.persistence.model.Selection;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.util.List;

import static server.util.ProtocolValidator.*;

public class DormitoryUserController {
    /**
     * @param protocol header(type:request, dataType: TLV, code: selection_schedule, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : selection_schedule, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getSelectionSchedule(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = result.getHeader();
        String id = (String) protocol.getChildren().getLast().getData();

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

    /**
     * @param protocol header(type:request, dataType: TLV, code: get_meal_plan, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : meal_plan, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getMealPlan(Protocol<?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        MealPlanDAO dao = new MealPlanDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();

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

    /**
     * @param protocol header(type:request, dataType: TLV, code: get_dormitory_room_type, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : dormitory_room_type, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getDormitoryRooms(Protocol<?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        RoomTypeDAO dao = new RoomTypeDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();

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

    /**
     * @param protocol header(type:request, dataType: TLV, code: select_priority_application, dataLength:)
     *                 data:
     *                 children <
     *                 1( header(type: value, dataType: string, code: preference, dataLength:,)
     *                 data: 세션아이디
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> selectPriorityApplication(Protocol<?> protocol) throws SQLException {
        Header header = protocol.getHeader();
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        Integer preference = (Integer) protocol.getChildren().getFirst().getData();

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

    /**
     * @param protocol header(type:request, dataType: TLV, code: apply_roommate, dataLength:)
     *                 data:
     *                 children <
     *                 1( header(type: value, dataType: string, code: user_id, dataLength:,)
     *                 data: 세션아이디
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */

    public static Protocol<?> applyRoommate(Protocol<?> protocol) throws SQLException {
        Header header = new Header();
        UserDAO dao = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            dao.updateRoommate(getIdBySessionId(id), (Integer) protocol.getChildren().getFirst().getData());
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
        String id = (String) protocol.getChildren().getLast().getData();
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
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            dao.updateRoomType(id, (String) protocol.getChildren().get(1).getData());
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
        String id = (String) protocol.getChildren().getLast().getData();
        String uid = getIdBySessionId(id);
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

    /**
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : demerit_point, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getMeritAndDemeritPoints(Protocol<?> protocol) throws SQLException {
        DemeritPointDAO dao = new DemeritPointDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            List<String> list = dao.findAllPointIntoString();

            for (String i : list) {
                Protocol<String> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setCode(Code.ValueCode.DEMERIT_POINT);
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

    public static Protocol<?> uploadTuberReport(Protocol<?> protocol) throws SQLException {
        UserDAO dao = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {

        }

        return result;
    }

    /**
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : demerit_point, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getFileForProof(Protocol<?> protocol) throws SQLException {
        SelectionDAO dao = new SelectionDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            SelectionDTO selectionDTO = dao.findByUid(getIdBySessionId(id));
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);

            Header childHeader = new Header();
            childHeader.setType(Type.VALUE);
            childHeader.setDataType(DataType.RAW);
            childHeader.setCode(Code.ValueCode.PROOF_FILE);
            Protocol<Byte[]> child = new Protocol<>();
            child.setHeader(childHeader);
            child.setData(selectionDTO.getAdditionalProofFileDTO().getData());
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        return result;
    }

    public static Protocol<?> uploadFileForProof(Protocol<?> protocol) throws SQLException {
        return null;
    }
}
