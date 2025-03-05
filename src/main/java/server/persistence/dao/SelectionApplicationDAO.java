package server.persistence.dao;

import server.persistence.dto.SelectionApplicationDTO;
import server.persistence.dto.SelectionApplicationStatusDTO;
import server.persistence.dto.SelectionScheduleDTO;
import server.persistence.dto.DormitoryRoomTypeDTO;
import server.persistence.dto.MealPlanDTO;
import server.persistence.dto.UserDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionApplicationDAO implements SelectionApplicationDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public SelectionApplicationDTO findById(Integer id) throws SQLException {
        String query = "SELECT sa.id AS application_id, sa.preference, sa.has_sleep_habit, sa.is_year_room, sa.created_at, sa.updated_at, " +
                "sa.selection_application_status_id, sa.selection_schedule_id, sa.dormitory_room_type_id, sa.meal_plan_id, " +
                "sa.roommate_user_id, sa.user_id, " +
                "sas.id AS status_id, sas.status_name, " +
                "ss.id AS schedule_id, ss.title, " +
                "drt.id AS room_type_id, drt.room_type_id, " +
                "mp.id AS meal_plan_id, mp.meal_plan_type_id, " +
                "u.id AS user_id, u.user_name " +
                "FROM selection_applications sa " +
                "LEFT JOIN selection_application_statuses sas ON sa.selection_application_status_id = sas.id " +
                "LEFT JOIN selection_schedules ss ON sa.selection_schedule_id = ss.id " +
                "LEFT JOIN dormitory_room_types drt ON sa.dormitory_room_type_id = drt.id " +
                "LEFT JOIN meal_plans mp ON sa.meal_plan_id = mp.id " +
                "LEFT JOIN users u ON sa.roommate_user_id = u.id " +
                "WHERE sa.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionApplicationDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // Uid를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public SelectionApplicationDTO findByUid(String uid) throws SQLException {
        String query = "SELECT sa.id AS application_id, sa.preference, sa.has_sleep_habit, sa.is_year_room, sa.created_at, sa.updated_at, " +
                "sa.selection_application_status_id, sa.selection_schedule_id, sa.dormitory_room_type_id, sa.meal_plan_id, " +
                "sa.roommate_user_id, sa.user_id, " +
                "sas.id AS status_id, sas.status_name, " +
                "ss.id AS schedule_id, ss.title, " +
                "drt.id AS room_type_id, drt.room_type_id, " +
                "mp.id AS meal_plan_id, mp.meal_plan_type_id, " +
                "u.id AS user_id, ru.user_name AS roommate_user_name " +
                "FROM selection_applications sa " +
                "JOIN selection_application_statuses sas ON sa.selection_application_status_id = sas.id " +
                "JOIN selection_schedules ss ON sa.selection_schedule_id = ss.id " +
                "JOIN dormitory_room_types drt ON sa.dormitory_room_type_id = drt.id " +
                "JOIN meal_plans mp ON sa.meal_plan_id = mp.id " +
                "JOIN users ru ON sa.roommate_user_id = ru.id " +
                "JOIN users u ON sa.user_id = u.id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionApplicationDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<SelectionApplicationDTO> findAll() throws SQLException {
        List<SelectionApplicationDTO> selectionApplications = new ArrayList<>();
        String query = "SELECT sa.id AS application_id, sa.preference, sa.has_sleep_habit, sa.is_year_room, sa.created_at, sa.updated_at, " +
                "sa.selection_application_status_id, sa.selection_schedule_id, sa.dormitory_room_type_id, sa.meal_plan_id, " +
                "sa.roommate_user_id, sa.user_id, " +
                "sas.id AS status_id, sas.status_name, " +
                "ss.id AS schedule_id, ss.title, " +
                "drt.id AS room_type_id, drt.room_type_id, " +
                "mp.id AS meal_plan_id, mp.meal_plan_type_id, " +
                "u.id AS user_id, u.user_name " +
                "FROM selection_applications sa " +
                "JOIN selection_application_statuses sas ON sa.selection_application_status_id = sas.id " +
                "JOIN selection_schedules ss ON sa.selection_schedule_id = ss.id " +
                "JOIN dormitory_room_types drt ON sa.dormitory_room_type_id = drt.id " +
                "JOIN meal_plans mp ON sa.meal_plan_id = mp.id " +
                "JOIN users ru ON sa.roommate_user_id = ru.id " +
                "JOIN users u ON sa.user_id = u.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionApplications.add(mapRowToSelectionApplicationDTO(resultSet));
            }
        }
        return selectionApplications; // 모든 선택 신청 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(SelectionApplicationDTO selectionApplicationDTO) throws SQLException {
        String query = "INSERT INTO selection_applications (preference, has_sleep_habit, is_year_room, created_at, updated_at, " +
                "selection_application_status_id, selection_schedule_id, dormitory_room_type_id, meal_plan_id, roommate_user_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionApplicationDTO.getPreference());
            preparedStatement.setBoolean(2, selectionApplicationDTO.isHasSleepHabit());
            preparedStatement.setBoolean(3, selectionApplicationDTO.isYear());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionApplicationDTO.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(selectionApplicationDTO.getUpdatedAt()));
            preparedStatement.setInt(6, selectionApplicationDTO.getSelectionApplicationStatusDTO() != null ? selectionApplicationDTO.getSelectionApplicationStatusDTO().getId() : null);
            preparedStatement.setInt(7, selectionApplicationDTO.getSelectionScheduleDTO() != null ? selectionApplicationDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.setInt(8, selectionApplicationDTO.getDormitoryRoomTypeDTO() != null ? selectionApplicationDTO.getDormitoryRoomTypeDTO().getId() : null);
            preparedStatement.setInt(9, selectionApplicationDTO.getMealPlanDTO() != null ? selectionApplicationDTO.getMealPlanDTO().getId() : null);
            preparedStatement.setInt(10, selectionApplicationDTO.getRoommateUserDTO() != null ? selectionApplicationDTO.getRoommateUserDTO().getId() : null);
            preparedStatement.setInt(11, selectionApplicationDTO.getUserDTO() != null ? selectionApplicationDTO.getUserDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(SelectionApplicationDTO selectionApplicationDTO) throws SQLException {
        String query = "UPDATE selection_applications SET preference = ?, has_sleep_habit = ?, is_year_room = ?, " +
                "created_at = ?, updated_at = ?, selection_application_status_id = ?, selection_schedule_id = ?, " +
                "dormitory_room_type_id = ?, meal_plan_id = ?, roommate_user_id = ?, user_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionApplicationDTO.getPreference());
            preparedStatement.setBoolean(2, selectionApplicationDTO.isHasSleepHabit());
            preparedStatement.setBoolean(3, selectionApplicationDTO.isYear());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionApplicationDTO.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(selectionApplicationDTO.getUpdatedAt()));
            preparedStatement.setInt(6, selectionApplicationDTO.getSelectionApplicationStatusDTO() != null ? selectionApplicationDTO.getSelectionApplicationStatusDTO().getId() : null);
            preparedStatement.setInt(7, selectionApplicationDTO.getSelectionScheduleDTO() != null ? selectionApplicationDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.setInt(8, selectionApplicationDTO.getDormitoryRoomTypeDTO() != null ? selectionApplicationDTO.getDormitoryRoomTypeDTO().getId() : null);
            preparedStatement.setInt(9, selectionApplicationDTO.getMealPlanDTO() != null ? selectionApplicationDTO.getMealPlanDTO().getId() : null);
            preparedStatement.setInt(10, selectionApplicationDTO.getRoommateUserDTO() != null ? selectionApplicationDTO.getRoommateUserDTO().getId() : null);
            preparedStatement.setInt(11, selectionApplicationDTO.getUserDTO() != null ? selectionApplicationDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(12, selectionApplicationDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Uid를 통해 검색하고, 우선도를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void updatePreference(String uid, Integer preference) throws SQLException {
        String query = "SELECT s.id FROM users u " +
                "INNER JOIN selection_applications s ON u.id = s.user_id " +
                "WHERE u.uid = ?";
        int id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,uid);

            ResultSet resultSet = preparedStatement.executeQuery();

            id = resultSet.getInt(1);
        }

        query = "UPDATE selection_applications SET preference = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, preference);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    // uid를 통해 검색하고, mealPlanName을 통해 상태를 각각 가져와, 업데이트 하는 메서드이다.
    @Override
    public void updateMealPlan(String uid, String mealPlanName) throws SQLException {
        String query = "SELECT s.id FROM selection_applications s " +
                "LEFT JOIN users u ON u.uid = ?";
        int user_id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            user_id = resultSet.getInt(1);
        }

        query = "SELECT m.id FROM meal_plans m " +
                "INNER JOIN meal_plan_types mpt ON mpt.type_name = ?";
        int meal_plan_id;
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,mealPlanName);
            ResultSet resultSet = preparedStatement.executeQuery();
            meal_plan_id = resultSet.getInt(1);
        }

        query = "UPDATE selection_applications SET meal_plan_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, meal_plan_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.executeUpdate();
        }
    }

    // uid를 통해 정보를 검색하고, 상태 이름을 통해 id를 각각 가져와, 업데이트 하는 메서드이다.
    @Override
    public void updateSelectionApplication(String uid, String statusName) throws SQLException {
        String query = "SELECT sa.id, sas.id " +
                "FROM selection_applications sa " +
                "INNER JOIN selection_application_statuses sas ON sas.status_name = ? " +
                "INNER JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        int id;
        int status_id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, statusName);
            preparedStatement.setString(2,uid);

            ResultSet resultSet = preparedStatement.executeQuery();

            id = resultSet.getInt(1);
            status_id = resultSet.getInt(2);
        }

        query = "UPDATE selection_applications SET selection_application_status_id = ?" +
                "WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1,status_id);
            preparedStatement.setInt(2,id);
            preparedStatement.executeUpdate();
        }
    }

    // Uid를 통해 검색하고, 기숙사 형태를 검색하여 id를 각각 가져와, 업데이트 하는 메서드이다.
    @Override
    public void updateRoomType(String uid, String roomTypeName) throws SQLException {
        String query = "SELECT s.id FROM selection_applications s " +
                "LEFT JOIN users u ON u.uid = ?";
        int user_id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            user_id = resultSet.getInt(1);
        }

        query = "SELECT drt.id FROM dormitory_room_types drt " +
                "INNER JOIN room_types rt ON rt.type_name = ?";
        int drt_id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,roomTypeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            drt_id = resultSet.getInt(1);
        }

        query = "UPDATE  selection_applications SET dormitory_room_type_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, drt_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_applications WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private SelectionApplicationDTO mapRowToSelectionApplicationDTO(ResultSet resultSet) throws SQLException {
        SelectionApplicationStatusDAO dao1 = new SelectionApplicationStatusDAO();
        SelectionApplicationStatusDTO statusDTO = dao1.findById(resultSet.getInt("selection_application_status_id"));

        SelectionScheduleDAO dao2 = new SelectionScheduleDAO();
        SelectionScheduleDTO scheduleDTO = dao2.findById(resultSet.getInt("selection_schedule_id"));

        DormitoryRoomTypeDAO dao3 = new DormitoryRoomTypeDAO();
        DormitoryRoomTypeDTO roomTypeDTO = dao3.findById(resultSet.getInt("dormitory_room_type_id"));

        MealPlanDAO dao4 = new MealPlanDAO();
        MealPlanDTO mealPlanDTO = dao4.findById(resultSet.getInt("meal_plan_id"));

        UserDAO dao5 = new UserDAO();
        UserDTO roommateUserDTO = dao5.findById(resultSet.getInt("roommate_user_id"));

        UserDTO userDTO = dao5.findById(resultSet.getInt("user_id"));

        return SelectionApplicationDTO.builder()
                .id(resultSet.getInt("application_id"))
                .preference(resultSet.getInt("preference"))
                .hasSleepHabit(resultSet.getBoolean("has_sleep_habit"))
                .isYear(resultSet.getBoolean("is_year"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .selectionApplicationStatusDTO(statusDTO) // 선택 신청 상태 정보 추가
                .selectionScheduleDTO(scheduleDTO) // 선택 일정 정보 추가
                .dormitoryRoomTypeDTO(roomTypeDTO) // 기숙사 방 타입 정보 추가
                .mealPlanDTO(mealPlanDTO) // 식사 계획 정보 추가
                .roommateUserDTO(roommateUserDTO) // 룸메이트 사용자 정보 추가
                .userDTO(userDTO) // 사용자 정보 추가
                .build();
    }
}

