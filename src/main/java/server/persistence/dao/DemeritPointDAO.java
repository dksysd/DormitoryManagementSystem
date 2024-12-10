package server.persistence.dao;

import server.persistence.dto.DemeritPointDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemeritPointDAO implements DemeritPointDAOI {

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

    @Override
    public List<String> findAllPointIntoString() throws SQLException {
        List<String> demeritPoints = new ArrayList<>();
        String query = "SELECT u.uid AS uid, dp.point AS point FROM demerit_points dp" +
                "INNER JOIN users u ON dp.user_id = u.id" +
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

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM demerit_points WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private DemeritPointDTO mapRowToDemeritPointDTO(ResultSet resultSet) throws SQLException {
        return DemeritPointDTO.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .point(resultSet.getInt("point"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
