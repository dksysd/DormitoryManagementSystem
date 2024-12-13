package server.persistence.dao;

import server.persistence.dto.DemeritPointDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.RoomAssignmentDTO;
import server.persistence.dto.UserDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemeritPointDAO implements DemeritPointDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public DemeritPointDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, description, point, created_at, user_id, room_assignment_id FROM demerit_points WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToDemeritPointDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<DemeritPointDTO> findAll() throws SQLException {
        List<DemeritPointDTO> demeritPoints = new ArrayList<>();
        String query = "SELECT id, description, point, created_at, user_id, room_assignment_id FROM demerit_points";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                demeritPoints.add(mapRowToDemeritPointDTO(resultSet));
            }
        }
        return demeritPoints; // 모든 불이익 점수 정보 반환
    }

    // DB의 모든 항목들을 가져와서 List<String>으로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<String> findAllPointIntoString() throws SQLException {
        List<String> demeritPoints = new ArrayList<>();
        String query = "SELECT u.uid AS uid, dp.point AS point FROM demerit_points dp " +
                "INNER JOIN users u ON dp.user_id = u.id " +
                "WHERE u.uid = ?";
        try(Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                demeritPoints.add(resultSet.getString(1) + " : " +  resultSet.getInt(1));
            }
        }

        return demeritPoints;
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(DemeritPointDTO demeritPointDTO) throws SQLException {
        String query = "INSERT INTO demerit_points (description, created_at, user_id, room_assignment_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, demeritPointDTO.getDescription());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(demeritPointDTO.getCreatedAt()));
            preparedStatement.setInt(3, demeritPointDTO.getUserDTO() != null ? demeritPointDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(4, demeritPointDTO.getRoomAssignmentDTO() != null ? demeritPointDTO.getRoomAssignmentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // 필요한 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void savePoint(String uid, String description, int score) throws SQLException{
        int user_id = -1;
        int room_assignment_id = -1;
        String query = "SELECT u.id AS user_id, ra.id AS room_id FROM users u " +
                "INNER JOIN selection_applications sa ON u.id = sa.user_id " +
                "INNER JOIN selections s ON sa.id = s.selection_application_id " +
                "INNER JOIN room_assignments ra ON s.id = ra.selection_id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,uid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user_id = resultSet.getInt(1);
                room_assignment_id = resultSet.getInt(2);
            }
        }
        if (user_id != -1 && room_assignment_id != -1) {
            query = "INSERT INTO demerit_points (description, created_at, user_id, room_assignment_id) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseConnectionPool.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, description);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setInt(3, user_id);
                preparedStatement.setInt(4, room_assignment_id);
                preparedStatement.executeUpdate();
            }
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(DemeritPointDTO demeritPointDTO) throws SQLException {
        String query = "UPDATE demerit_points SET description = ?, created_at = ?, user_id = ?, room_assignment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, demeritPointDTO.getDescription());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(demeritPointDTO.getCreatedAt()));
            preparedStatement.setInt(3, demeritPointDTO.getUserDTO() != null ? demeritPointDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(4, demeritPointDTO.getRoomAssignmentDTO() != null ? demeritPointDTO.getRoomAssignmentDTO().getId() : null);
            preparedStatement.setInt(5, demeritPointDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM demerit_points WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private DemeritPointDTO mapRowToDemeritPointDTO(ResultSet resultSet) throws SQLException {
        UserDAO dao1 = new UserDAO();
        UserDTO userDTO = dao1.findById(resultSet.getInt("user_id"));

        RoomAssignmentDAO dao2 = new RoomAssignmentDAO();
        RoomAssignmentDTO roomAssignmentDTO = dao2.findById(resultSet.getInt("room_assignment_id"));

        return DemeritPointDTO.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .point(resultSet.getInt("point"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .userDTO(userDTO)
                .roomAssignmentDTO(roomAssignmentDTO)
                .build();
    }
}
