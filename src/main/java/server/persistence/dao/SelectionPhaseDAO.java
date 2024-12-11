package server.persistence.dao;

import server.persistence.dto.SelectionPhaseDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.SelectionScheduleDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionPhaseDAO implements SelectionPhaseDAOI {

    @Override
    public SelectionPhaseDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id FROM selection_phases WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionPhaseDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public SelectionPhaseDTO findByName(String name) throws SQLException {
        String query = "Select id, phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id FROM selection_phases WHERE phase_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionPhaseDTO(resultSet);
            }
        }
        return null;
    }
    @Override
    public List<SelectionPhaseDTO> findAll() throws SQLException {
        List<SelectionPhaseDTO> selectionPhases = new ArrayList<>();
        String query = "SELECT id, phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id FROM selection_phases";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionPhases.add(mapRowToSelectionPhaseDTO(resultSet));
            }
        }
        return selectionPhases; // 모든 선택 단계 정보 반환
    }

    @Override
    public void save(SelectionPhaseDTO selectionPhaseDTO) throws SQLException {
        String query = "INSERT INTO selection_phases (phase_name, description, start_at, end_at, created_at, updated_at, selection_schedule_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionPhaseDTO.getPhaseName());
            preparedStatement.setString(2, selectionPhaseDTO.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionPhaseDTO.getStartAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionPhaseDTO.getEndAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(selectionPhaseDTO.getCreatedAt()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(selectionPhaseDTO.getUpdatedAt()));
            preparedStatement.setInt(7, selectionPhaseDTO.getSelectionScheduleDTO() != null ? selectionPhaseDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionPhaseDTO selectionPhaseDTO) throws SQLException {
        String query = "UPDATE selection_phases SET phase_name = ?, description = ?, start_at = ?, end_at = ?, created_at = ?, updated_at = ?, selection_schedule_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, selectionPhaseDTO.getPhaseName());
            preparedStatement.setString(2, selectionPhaseDTO.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionPhaseDTO.getStartAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(selectionPhaseDTO.getEndAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(selectionPhaseDTO.getCreatedAt()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(selectionPhaseDTO.getUpdatedAt()));
            preparedStatement.setInt(7, selectionPhaseDTO.getSelectionScheduleDTO() != null ? selectionPhaseDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.setInt(8, selectionPhaseDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_phases WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SelectionPhaseDTO mapRowToSelectionPhaseDTO(ResultSet resultSet) throws SQLException {
        SelectionScheduleDTO selectionScheduleDTO = SelectionScheduleDTO.builder()
                .id(resultSet.getInt("selection_schedule_id"))
                .build();

        return SelectionPhaseDTO.builder()
                .id(resultSet.getInt("id"))
                .phaseName(resultSet.getString("phase_name"))
                .description(resultSet.getString("description"))
                .startAt(resultSet.getTimestamp("start_at").toLocalDateTime())
                .endAt(resultSet.getTimestamp("end_at").toLocalDateTime())
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .selectionScheduleDTO(selectionScheduleDTO) // ID만 세팅
                .build();
    }
}
