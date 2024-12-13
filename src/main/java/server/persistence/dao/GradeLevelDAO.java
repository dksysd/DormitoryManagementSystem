package server.persistence.dao;

import server.persistence.dto.GradeLevelDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeLevelDAO implements GradeLevelDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // 이름을 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM grade_levels WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private GradeLevelDTO mapRowToGradeLevelDTO(ResultSet resultSet) throws SQLException {
        return GradeLevelDTO.builder()
                .id(resultSet.getInt("id"))
                .levelName(resultSet.getString("level_name"))
                .scaledScore(resultSet.getFloat("scaled_score"))
                .build();
    }
}
