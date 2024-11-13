package server.persistence.dao;

import server.persistence.dto.GradeLevelDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeLevelDAO implements GradeLevelDAOI {

    @Override
    public GradeLevelDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, level_name, scaled_score FROM grade_levels WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToGradeLevelDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public GradeLevelDTO findByName(String name) throws SQLException {
        String query = "Select * from grade_levels where level_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToGradeLevelDTO(resultSet);
            }
        }

        return null;
    }

    @Override
    public List<GradeLevelDTO> findAll() throws SQLException {
        List<GradeLevelDTO> gradeLevels = new ArrayList<>();
        String query = "SELECT id, level_name, scaled_score FROM grade_levels";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                gradeLevels.add(mapRowToGradeLevelDTO(resultSet));
            }
        }
        return gradeLevels; // 모든 성적 수준 정보 반환
    }

    @Override
    public void save(GradeLevelDTO gradeLevelDTO) throws SQLException {
        String query = "INSERT INTO grade_levels (level_name, scaled_score) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, gradeLevelDTO.getLevelName());
            preparedStatement.setFloat(2, gradeLevelDTO.getScaledScore());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(GradeLevelDTO gradeLevelDTO) throws SQLException {
        String query = "UPDATE grade_levels SET level_name = ?, scaled_score = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, gradeLevelDTO.getLevelName());
            preparedStatement.setFloat(2, gradeLevelDTO.getScaledScore());
            preparedStatement.setInt(3, gradeLevelDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM grade_levels WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private GradeLevelDTO mapRowToGradeLevelDTO(ResultSet resultSet) throws SQLException {
        return GradeLevelDTO.builder()
                .id(resultSet.getInt("id"))
                .levelName(resultSet.getString("level_name"))
                .scaledScore(resultSet.getFloat("scaled_score"))
                .build();
    }
}
