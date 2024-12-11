package server.persistence.dao;

import server.persistence.dto.SelectionApplicationStatusDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionApplicationStatusDAO implements SelectionApplicationStatusDAOI {

    @Override
    public SelectionApplicationStatusDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, status_name, description FROM selection_application_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionApplicationStatusDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public SelectionApplicationStatusDTO findByName(String name) throws SQLException {
        String query = "SELECT id, status_name, description FROM selection_application_statuses WHERE status_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionApplicationStatusDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<SelectionApplicationStatusDTO> findAll() throws SQLException {
        List<SelectionApplicationStatusDTO> statuses = new ArrayList<>();
        String query = "SELECT id, status_name, description FROM selection_application_statuses";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                statuses.add(mapRowToSelectionApplicationStatusDTO(resultSet));
            }
        }
        return statuses; // 모든 선택 신청 상태 정보 반환
    }

    @Override
    public void save(SelectionApplicationStatusDTO selectionApplicationStatusDTO) throws SQLException {
        String query = "INSERT INTO selection_application_statuses (status_name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionApplicationStatusDTO.getStatusName());
            preparedStatement.setString(2, selectionApplicationStatusDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionApplicationStatusDTO selectionApplicationStatusDTO) throws SQLException {
        String query = "UPDATE selection_application_statuses SET status_name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionApplicationStatusDTO.getStatusName());
            preparedStatement.setString(2, selectionApplicationStatusDTO.getDescription());
            preparedStatement.setInt(3, selectionApplicationStatusDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_application_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SelectionApplicationStatusDTO mapRowToSelectionApplicationStatusDTO(ResultSet resultSet) throws SQLException {
        return SelectionApplicationStatusDTO.builder()
                .id(resultSet.getInt("id"))
                .statusName(resultSet.getString("status_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
