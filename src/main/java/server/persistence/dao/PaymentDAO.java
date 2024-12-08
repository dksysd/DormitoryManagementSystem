package server.persistence.dao;

import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements PaymentDAOI {

    @Override
    public PaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, payment_amount, created_at, payment_code_id, payment_status_id, payment_method_id FROM payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<PaymentDTO> findAll() throws SQLException {
        List<PaymentDTO> payments = new ArrayList<>();
        String query = "SELECT id, payment_amount, created_at, payment_code_id, payment_status_id, payment_method_id FROM payments";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                payments.add(mapRowToPaymentDTO(resultSet));
            }
        }
        return payments; // 모든 결제 정보 반환
    }

    @Override
    public Integer getPaymentAmountByUid(String uid) throws SQLException {
        String query = "SELECT payment_amount FROM payments WHERE uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return null;
    }
    @Override
    public void save(PaymentDTO paymentDTO) throws SQLException {
        String query = "INSERT INTO payments (payment_amount, created_at, payment_code_id, payment_status_id, payment_method_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, paymentDTO.getPaymentAmount());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(paymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, paymentDTO.getPaymentCodeDTO() != null ? paymentDTO.getPaymentCodeDTO().getId() : null);
            preparedStatement.setInt(4, paymentDTO.getPaymentStatusDTO() != null ? paymentDTO.getPaymentStatusDTO().getId() : null);
            preparedStatement.setInt(5, paymentDTO.getPaymentMethodDTO() != null ? paymentDTO.getPaymentMethodDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentDTO paymentDTO) throws SQLException {
        String query = "UPDATE payments SET payment_amount = ?, created_at = ?, payment_code_id = ?, payment_status_id = ?, payment_method_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, paymentDTO.getPaymentAmount());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(paymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, paymentDTO.getPaymentCodeDTO() != null ? paymentDTO.getPaymentCodeDTO().getId() : null);
            preparedStatement.setInt(4, paymentDTO.getPaymentStatusDTO() != null ? paymentDTO.getPaymentStatusDTO().getId() : null);
            preparedStatement.setInt(5, paymentDTO.getPaymentMethodDTO() != null ? paymentDTO.getPaymentMethodDTO().getId() : null);
            preparedStatement.setInt(6, paymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentDTO mapRowToPaymentDTO(ResultSet resultSet) throws SQLException {
        return PaymentDTO.builder()
                .id(resultSet.getInt("id"))
                .paymentAmount(resultSet.getInt("payment_amount"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
