package server.persistence.dao;

import server.persistence.dto.SelectionScheduleDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionScheduleDAO implements SelectionScheduleDAOI {

    @Override
    public SelectionScheduleDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, title, created_at FROM selection_schedules WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionScheduleDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<SelectionScheduleDTO> findAll() throws SQLException {
        List<SelectionScheduleDTO> selectionSchedules = new ArrayList<>();
        String query = "SELECT id, title, created_at FROM selection_schedules";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionSchedules.add(mapRowToSelectionScheduleDTO(resultSet));
            }
        }
        return selectionSchedules; // 모든 선택 일정 정보 반환
    }

    @Override
    public List<String> findAllTitleIntoString() throws SQLException {
        List<String> selectionSchedules = new ArrayList<>();
        String query = "SELECT id, title, created_at, started_at FROM selection_schedules";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionSchedules.add(resultSet.getString(2) + ", "
                + resultSet.getString(4));
            }
        }
        return selectionSchedules;
    }
    @Override
    public void save(SelectionScheduleDTO selectionScheduleDTO) throws SQLException {
        String query = "INSERT INTO selection_schedules (title, created_at) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionScheduleDTO.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionScheduleDTO.getCreatedAt()));
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionScheduleDTO selectionScheduleDTO) throws SQLException {
        String query = "UPDATE selection_schedules SET title = ?, created_at = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionScheduleDTO.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionScheduleDTO.getCreatedAt()));
            preparedStatement.setInt(3, selectionScheduleDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_schedules WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SelectionScheduleDTO mapRowToSelectionScheduleDTO(ResultSet resultSet) throws SQLException {
        return SelectionScheduleDTO.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
