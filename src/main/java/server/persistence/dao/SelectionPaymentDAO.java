package server.persistence.dao;

import server.persistence.dto.SelectionPaymentDTO;
import server.persistence.dto.SelectionDTO;
import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionPaymentDAO implements SelectionPaymentDAOI {

    @Override
    public SelectionPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT sp.id AS payment_id, sp.selection_id, sp.payment_id, " +
                "s.id AS selection_id, s.is_final_approved, s.created_at AS selection_created_at, " +
                "p.id AS payment_id, p.amount, p.created_at AS payment_created_at " +
                "FROM selection_payments sp " +
                "LEFT JOIN selections s ON sp.selection_id = s.id " +
                "LEFT JOIN payments p ON sp.payment_id = p.id " +
                "WHERE sp.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<SelectionPaymentDTO> findAll() throws SQLException {
        List<SelectionPaymentDTO> selectionPayments = new ArrayList<>();
        String query = "SELECT sp.id AS payment_id, sp.selection_id, sp.payment_id, " +
                "s.id AS selection_id, s.is_final_approved, s.created_at AS selection_created_at, " +
                "p.id AS payment_id, p.amount, p.created_at AS payment_created_at " +
                "FROM selection_payments sp " +
                "LEFT JOIN selections s ON sp.selection_id = s.id " +
                "LEFT JOIN payments p ON sp.payment_id = p.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionPayments.add(mapRowToSelectionPaymentDTO(resultSet));
            }
        }
        return selectionPayments; // 모든 선택 결제 정보 반환
    }

    @Override
    public void save(SelectionPaymentDTO selectionPaymentDTO) throws SQLException {
        String query = "INSERT INTO selection_payments (selection_id, payment_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionPaymentDTO.getSelectionDTO() != null ? selectionPaymentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(2, selectionPaymentDTO.getPaymentDTO() != null ? selectionPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionPaymentDTO selectionPaymentDTO) throws SQLException {
        String query = "UPDATE selection_payments SET selection_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionPaymentDTO.getSelectionDTO() != null ? selectionPaymentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(2, selectionPaymentDTO.getPaymentDTO() != null ? selectionPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(3, selectionPaymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SelectionPaymentDTO mapRowToSelectionPaymentDTO(ResultSet resultSet) throws SQLException {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .id(resultSet.getInt("selection_id"))
                .isFinalApproved(resultSet.getBoolean("is_final_approved"))
                .createdAt(resultSet.getTimestamp("selection_created_at").toLocalDateTime())
                .build();

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .id(resultSet.getInt("payment_id"))
                .paymentAmount(resultSet.getInt("payment_amount")) // 결제 금액 추가
                .createdAt(resultSet.getTimestamp("payment_created_at").toLocalDateTime())
                .build();

        return SelectionPaymentDTO.builder()
                .id(resultSet.getInt("payment_id"))
                .selectionDTO(selectionDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
