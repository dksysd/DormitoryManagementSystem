package server.persistence.dao;

import server.persistence.dto.DormitoryDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormitoryDAO implements DormitoryDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public DormitoryDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, name, description FROM dormitories WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToDormitoryDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<DormitoryDTO> findAll() throws SQLException {
        List<DormitoryDTO> dormitories = new ArrayList<>();
        String query = "SELECT id, name, description FROM dormitories";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                dormitories.add(mapRowToDormitoryDTO(resultSet));
            }
        }
        return dormitories; // 모든 기숙사 정보 반환
    }

    // DB의 모든 항목들을 가져와서 List<String>로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<String> findAllIntoString() throws SQLException {
        List<String> dormitories = new ArrayList<>();
        String query = "SELECT id, name, description FROM dormitories";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                dormitories.add(resultSet.getString(2));
            }
        }

        return dormitories;
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(DormitoryDTO dormitoryDTO) throws SQLException {
        String query = "INSERT INTO dormitories (name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, dormitoryDTO.getName());
            preparedStatement.setString(2, dormitoryDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(DormitoryDTO dormitoryDTO) throws SQLException {
        String query = "UPDATE dormitories SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, dormitoryDTO.getName());
            preparedStatement.setString(2, dormitoryDTO.getDescription());
            preparedStatement.setInt(3, dormitoryDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM dormitories WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private DormitoryDTO mapRowToDormitoryDTO(ResultSet resultSet) throws SQLException {
        return DormitoryDTO.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
