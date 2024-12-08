package server.persistence.dao;

import server.persistence.dto.MoveOutRequestDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveOutRequestDAO implements MoveOutRequestDAOI {

    @Override
    public MoveOutRequestDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, bank_id FROM move_out_requests WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMoveOutRequestDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<MoveOutRequestDTO> findAll() throws SQLException {
        List<MoveOutRequestDTO> moveOutRequests = new ArrayList<>();
        String query = "SELECT id, checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, bank_id FROM move_out_requests";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                moveOutRequests.add(mapRowToMoveOutRequestDTO(resultSet));
            }
        }
        return moveOutRequests; // 모든 퇴실 요청 정보 반환
    }

    @Override
    public void save(MoveOutRequestDTO moveOutRequestDTO) throws SQLException {
        String query = "INSERT INTO move_out_requests (checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, bank_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(moveOutRequestDTO.getCheckoutAt()));
            preparedStatement.setString(2, moveOutRequestDTO.getAccountNumber());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(moveOutRequestDTO.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(moveOutRequestDTO.getUpdatedAt()));
            preparedStatement.setInt(5, moveOutRequestDTO.getMoveOutRequestStatusDTO() != null ? moveOutRequestDTO.getMoveOutRequestStatusDTO().getId() : null);
            preparedStatement.setInt(6, moveOutRequestDTO.getSelectionDTO() != null ? moveOutRequestDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(7, moveOutRequestDTO.getBankDTO() != null ? moveOutRequestDTO.getBankDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(MoveOutRequestDTO moveOutRequestDTO) throws SQLException {
        String query = "UPDATE move_out_requests SET checkout_at = ?, account_number = ?, created_at = ?, updated_at = ?, move_out_request_status_id = ?, selection_id = ?, bank_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(moveOutRequestDTO.getCheckoutAt()));
            preparedStatement.setString(2, moveOutRequestDTO.getAccountNumber());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(moveOutRequestDTO.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(moveOutRequestDTO.getUpdatedAt()));
            preparedStatement.setInt(5, moveOutRequestDTO.getMoveOutRequestStatusDTO() != null ? moveOutRequestDTO.getMoveOutRequestStatusDTO().getId() : null);
            preparedStatement.setInt(6, moveOutRequestDTO.getSelectionDTO() != null ? moveOutRequestDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(7, moveOutRequestDTO.getBankDTO() != null ? moveOutRequestDTO.getBankDTO().getId() : null);
            preparedStatement.setInt(8, moveOutRequestDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM move_out_requests WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private MoveOutRequestDTO mapRowToMoveOutRequestDTO(ResultSet resultSet) throws SQLException {
        return MoveOutRequestDTO.builder()
                .id(resultSet.getInt("id"))
                .checkoutAt(resultSet.getTimestamp("checkout_at").toLocalDateTime())
                .accountNumber(resultSet.getString("account_number"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}
