package server.controller;

import server.persistence.dao.*;
import server.persistence.dto.*;
import shared.protocol.persistence.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static server.util.ProtocolValidator.*;

public class DormitoryUserController {
    /**
     * 선발 일정에 있는 모든 값을 가져와서 프로토콜에 다시 전송하는 메서드이다.
     * @param protocol header(type:request, dataType: TLV, code: selection_schedule, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : selection_schedule, dataLength :, ))(우선)
     * 2 (일반)
     * 3(추가)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> getSelectionSchedule(Protocol<?> protocol) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader;
        String sessionId = (String) protocol.getChildren().getLast().getData();

        if (verifySessionId(sessionId) && isStudent(sessionId)) {
            List<String> list = dao.findAllTitleIntoString();

            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);

                Protocol<String> child = new Protocol<>();
                Header childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.SELECTION_SCHEDULE);
                child.setHeader(childHeader);
                child.setData(s);

                result.addChild(child);
            }


            resultHeader = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        }
        else {
           resultHeader = new Header(Type.ERROR, DataType.TLV, Code.ErrorCode.UNAUTHORIZED, 0);
        }
        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 선발 신청을 받아와서 그것을 토대로 선발 테이블에 새 데이터를 추가하는 메서드이다.
     * @param protocol header(type:request, dataType: TLV, code: select_priority_application, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: ID, dataLength:,)
     *                 data: 학번
     *                 2 ( header(type: value, dataType: boolean, code: sleep_hablit, dataLength:,)
     *                 data: 잠버릇
     *                 3 ( header(type: value, dataType: boolean, code: is_year, dataLength:,)
     *                 data: 일년입사
     *                 4 ( header(type: value, dataType: string, code: room_type, dataLength:,)
     *                 data: 생활관
     *                 5 ( header(type: value, dataType: string, code: meal_plan, dataLength:,)
     *                 data: 식사형식
     *                 6 ( header(type: value, dataType: string, code: ID, dataLength:,)
     *                 data: 룸메이트
     *                 7 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> application(Protocol<?> protocol) throws SQLException {
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        UserDAO userDAO = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(sessionId)&&isStudent(sessionId)) {
            RoomTypeDAO roomTypeDAO = new RoomTypeDAO();
            DormitoryDAO dormitoryDAO = new DormitoryDAO();
            DormitoryRoomTypeDTO dormitoryRoomTypeDTO = DormitoryRoomTypeDTO.builder()
                    .dormitoryDTO(dormitoryDAO.findByName((String) protocol.getChildren().get(3).getData()))
                    .build();
            MealPlanTypeDAO mealPlanTypeDAO = new MealPlanTypeDAO();
            MealPlanDTO mealPlanDTO = MealPlanDTO.builder()
                    .mealPlanTypeDTO(mealPlanTypeDAO.findByName((String) protocol.getChildren().get(4).getData()))
                    .build();


            // TODO : 현재 기간이 일반선발인지, 우선선발인지 판단하는 기능 필요. 이후 DTO에 새로 추가 바람.
            SelectionApplicationDTO selectionApplicationDTO = new SelectionApplicationDTO();
            selectionApplicationDTO.setUserDTO(userDAO.findByUid(getIdBySessionId(sessionId)));
            selectionApplicationDTO.setPreference((int) protocol.getChildren().getFirst().getData());
            selectionApplicationDTO.setHasSleepHabit((boolean) protocol.getChildren().get(1).getData());
            selectionApplicationDTO.setYear((boolean) protocol.getChildren().get(2).getData());
            selectionApplicationDTO.setCreatedAt(LocalDateTime.now());
            selectionApplicationDTO.setUpdatedAt(LocalDateTime.now());
            selectionApplicationDTO.setDormitoryRoomTypeDTO(dormitoryRoomTypeDTO);
            selectionApplicationDTO.setMealPlanDTO(mealPlanDTO);
            if (protocol.getHeader().getDataLength() >= 7) {
                UserDTO userDTO = userDAO.findByUid((String) protocol.getChildren().get(5).getData());
                selectionApplicationDTO.setRoommateUserDTO(userDTO);
            }
            dao.save(selectionApplicationDTO);
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setDataType(DataType.TLV);
        } else {
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setType(Type.ERROR);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 모든 식사계획을 참조하여 프로톸ㄹ에 보내는 메서드이다.
     *
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

        MealPlanDAO dao = new MealPlanDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();

        if (verifySessionId(sessionId) && isStudent(sessionId)) {
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
            resultHeader.setType(Type.ERROR);
            resultHeader.setDataType(DataType.TLV);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 모든 기숙사 생활관을 가져와서 프로토콜로 보내는 메서드이다.
     *
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
        DormitoryDAO dao = new DormitoryDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();

        if (verifySessionId(id) && isStudent(id)) {
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
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        resultHeader.setDataType(DataType.TLV);
        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 사용자로부터 우선순위를 받아온 뒤, UPDATE 하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: select_priority_application, dataLength:)
     *                 data:
     *                 children <
     *                 1( header(type: value, dataType: string, code: preference, dataLength:,)
     *                 data: 선호도
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> selectPriorityApplication(Protocol<?> protocol) throws SQLException {
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String sessionId = (String) protocol.getChildren().getLast().getData();
        String id = getIdBySessionId(sessionId);
        Integer preference = (Integer) protocol.getChildren().getFirst().getData();

        if (verifySessionId(sessionId) && isStudent(id)) {
            dao.updatePreference(id, preference);
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }
        resultHeader.setDataType(DataType.TLV);
        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 룸메이트 정보를 사용자로부터 받아온 뒤, 그 학번에 해당하는 id를 가져와 UPDATE하는 메서드이다.
     *
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
        UserDAO dao = new UserDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        resultHeader.setDataType(DataType.TLV);
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id) && isStudent(id)) {
            dao.updateRoommate(getIdBySessionId(id), (String) protocol.getChildren().getFirst().getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);

        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 식사정보를 사용자로부터 받아온 뒤 이를 UPDATE하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: apply_meal, dataLength:)
     *                 data:
     *                 children <
     *                 1( header(type: value, dataType: string, code: meal_plan, dataLength:,)
     *                 data: 세션아이디
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> applyMeal(Protocol<?> protocol) throws SQLException {
        SelectionApplicationDAO dao = new SelectionApplicationDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            dao.updateMealPlan(id, (String) protocol.getChildren().getFirst().getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        resultHeader.setDataType(DataType.TLV);
        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 생활관 정보를 받아온 뒤, UPDATE하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: apply_room, dataLength:)
     *                 data:
     *                 children <
     *                 1( header(type: value, dataType: string, code: dormitory_room_type, dataLength:,)
     *                 data: 세션아이디
     *                 2 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data : null
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> applyRoom(Protocol<?> protocol) throws SQLException {
        DormitoryRoomTypeDAO dao = new DormitoryRoomTypeDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            dao.updateDormitory(id, (String) protocol.getChildren().getFirst().getData());
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setType(Type.RESPONSE);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        resultHeader.setDataType(DataType.TLV);
        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 선발 상태를 DB에서 가져온 뒤, 프로토콜로 실어 보내는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_selection_result, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : selection_status, dataLength :, ))
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */

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
            if (Objects.equals(statusName, "선발")) {
                DormitoryRoomTypeDAO dormitoryRoomTypeDAO = new DormitoryRoomTypeDAO();
                String room_type_name = dormitoryRoomTypeDAO.findByUid(getIdBySessionId(id)).getRoomTypeDTO().getTypeName();
                Protocol<String> childDormitoryRoomType = new Protocol<>();
                childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.DORMITORY_ROOM_TYPE);
                childDormitoryRoomType.setHeader(childHeader);
                childDormitoryRoomType.setData(room_type_name);
                result.addChild(childDormitoryRoomType);

                RoomAssignmentDAO roomAssignmentDAO = new RoomAssignmentDAO();
                int roomAssignment_id = roomAssignmentDAO.findByUid(getIdBySessionId(id)).getRoomDTO().getId();
                RoomDAO roomDAO = new RoomDAO();
                String room_number = roomDAO.findById(roomAssignment_id).getRoomNumber();
                Protocol<String> childRoomNum = new Protocol<>();
                childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.STRING);
                childHeader.setCode(Code.ResponseCode.ValueCode.ROOM_NUMBER);
                childRoomNum.setHeader(childHeader);
                childRoomNum.setData(room_number);
                result.addChild(childRoomNum);

                int bedNumber = roomAssignmentDAO.findByUid(getIdBySessionId(id)).getBedNumber();
                Protocol<Integer> childBedNum = new Protocol<>();
                childHeader = new Header();
                childHeader.setType(Type.VALUE);
                childHeader.setDataType(DataType.INTEGER);
                childHeader.setCode(Code.ResponseCode.ValueCode.BED_NUMBER);
                childBedNum.setHeader(childHeader);
                childBedNum.setData(bedNumber);
                result.addChild(childBedNum);
            }

        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 상벌점 내역을 가져와서 프로토콜에 실어 보내는 메서드이다.
     *
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
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 퇴사 정보를 가져오는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : check_move_out, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> checkMoveOut(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id) && isStudent(id)) {
            UserDAO dao = new UserDAO();
            String check = dao.checkMoveOut(getIdBySessionId(id));
            Protocol<String> child = new Protocol<>();
            Header childHeader = new Header();
            childHeader.setType(Type.VALUE);
            childHeader.setDataType(DataType.STRING);
            childHeader.setCode(Code.ValueCode.MOVE_OUT_STATUS);
            resultHeader.setType(Type.RESPONSE);
            resultHeader.setCode(Code.ResponseCode.OK);
            resultHeader.setDataType(DataType.TLV);
            child.setHeader(childHeader);
            child.setData(check);
            result.addChild(child);
        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
            resultHeader.setDataType(DataType.TLV);
        }

        result.setHeader(resultHeader);
        return result;
    }

    /**
     * 결핵 진단서 이미지 파일을 받아와서 DB에 UPLOAD하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: raw, code: tuber_image, dataLength:,)
     *                 data: 이미지 )
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : upload_tuber, dataLength :, ))
     * 2 ...(이렇게 끝까지 반복되서 옴)
     * @return (에러의 경우) header(type : Response, dataType : TLV, code : Error dataLength: 0)
     * data: null
     */
    public static Protocol<?> uploadTuberReport(Protocol<?> protocol) throws SQLException {
        SelectionDAO dao = new SelectionDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            byte[] bytes = (byte[]) protocol.getChildren().getFirst().getData();
            Byte[] Bytes = new Byte[bytes.length];
            for (int b = 0; b < bytes.length; b++) {
                Bytes[b] = bytes[b];
            }
            dao.updateTuber(getIdBySessionId(id), Bytes);

            resultHeader.setType(Type.RESPONSE);
            resultHeader.setCode(Code.ResponseCode.OK);
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
     * 증거 파일을 가져오는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : get_proof, dataLength :, ))
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
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }

        return result;
    }

    /**
     * 결핵 진단서 이미지 파일을 받아와서 DB에 UPLOAD하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: get_merit_and_demerit_point, dataLength:)
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
     *1 ( header ( type : value, dataType : string, code : demerit_point, dataLength :, ))
     * @return (에러의 경우) header(type : Error, dataType : TLV, code : Error dataLength: 0)
     * 정상 -> header(type : Response, dataType : TLV, code : OK dataLength: 0)
     * data: null
     */

    public static Protocol<?> uploadFileForProof(Protocol<?> protocol) throws SQLException {
        SelectionDAO dao = new SelectionDAO();
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            Byte[] bytes = (Byte[]) protocol.getChildren().getFirst().getData();
            dao.updateProof(id, bytes);

            resultHeader.setType(Type.RESPONSE);
            resultHeader.setCode(Code.ResponseCode.OK);
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
     * 퇴사신청을 진행하는 메서드이다.
     *
     * @param protocol header(type:request, dataType: TLV, code: MOVE_OUT, dataLength:)
     *                 data:
     *                 children <
     *                 1 ( header(type: value, dataType: string, code: sessionId, dataLength:,)
     *                 data: 세션아이디 )
     *                 >
     * @return header(type : Response, dataType : TLV, code : OK, dataLength : 아래 갯수에 따라 다름.
     *data :
     *children <
     *1 ( header ( type : value, dataType : string, code : demerit_point, dataLength :, ))
     * @return (에러의 경우) header(type : Error, dataType : TLV, code : Error dataLength: 0)
     * 정상 -> header(type : Response, dataType : TLV, code : OK dataLength: 0)
     * data: null
     */
    public static Protocol<?> moveOut(Protocol<?> protocol) throws SQLException {
        Protocol<?> result = new Protocol<>();
        Header resultHeader = new Header(Type.RESPONSE, DataType.TLV, Code.ResponseCode.OK, 0);
        SelectionApplicationDAO selectionApplicationDAO = new SelectionApplicationDAO();
        MoveOutRequestDAO requestDAO = new MoveOutRequestDAO();
        String id = (String) protocol.getChildren().getLast().getData();
        if (verifySessionId(id)) {
            if (Objects.equals(selectionApplicationDAO.findByUid(id).getSelectionApplicationStatusDTO().getStatusName(), "선발")) {
                requestDAO.updateStatus(getIdBySessionId(id), "퇴사대기");
                SelectionDAO selectionDAO = new SelectionDAO();

                SelectionDTO selectionDTO = selectionDAO.findByUid(getIdBySessionId(id));
                MoveOutRequestDTO dto = MoveOutRequestDTO.builder()
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .selectionDTO(selectionDTO)
                        .build();
                requestDAO.save(dto);


            } else {
                resultHeader.setType(Type.ERROR);
                resultHeader.setCode(Code.ErrorCode.INVALID_REQUEST);
            }

        } else {
            resultHeader.setType(Type.ERROR);
            resultHeader.setCode(Code.ErrorCode.UNAUTHORIZED);
        }
        result.setHeader(resultHeader);
        return result;
    }
}
