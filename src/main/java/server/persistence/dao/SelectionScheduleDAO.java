package server.persistence.dao;

import server.persistence.dto.SelectionScheduleDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionScheduleDAO implements SelectionScheduleDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public SelectionScheduleDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, title, created_at, started_at, ended_at FROM selection_schedules WHERE id = ?";
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<SelectionScheduleDTO> findAll() throws SQLException {
        List<SelectionScheduleDTO> selectionSchedules = new ArrayList<>();
        String query = "SELECT id, title, created_at, started_at, ended_at FROM selection_schedules";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionSchedules.add(mapRowToSelectionScheduleDTO(resultSet));
            }
        }
        return selectionSchedules; // 모든 선택 일정 정보 반환
    }

    // DB의 모든 항목들을 가져와서 List<String>로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<String> findAllTitleIntoString() throws SQLException {
        List<String> selectionSchedules = new ArrayList<>();
        String query = "SELECT id, title, created_at, started_at, ended_at FROM selection_schedules";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionSchedules.add(resultSet.getString(2) + " "
                + resultSet.getString(4) + " "
                + resultSet.getString(5));
            }
        }
        return selectionSchedules;
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(SelectionScheduleDTO selectionScheduleDTO) throws SQLException {
        String query = "INSERT INTO selection_schedules (title, created_at, started_at, ended_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionScheduleDTO.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionScheduleDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionScheduleDTO.getStartedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionScheduleDTO.getEndedAt()));
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(SelectionScheduleDTO selectionScheduleDTO) throws SQLException {
        String query = "UPDATE selection_schedules SET title = ?, created_at = ?, started_at = ?, ended_at = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionScheduleDTO.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionScheduleDTO.getCreatedAt()));
            preparedStatement.setInt(3, selectionScheduleDTO.getId());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionScheduleDTO.getStartedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(selectionScheduleDTO.getEndedAt()));
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_schedules WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private SelectionScheduleDTO mapRowToSelectionScheduleDTO(ResultSet resultSet) throws SQLException {
        return SelectionScheduleDTO.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .startedAt(resultSet.getTimestamp("started_at").toLocalDateTime())
                .endedAt(resultSet.getTimestamp("ended_at").toLocalDateTime())
                .build();
    }
}
