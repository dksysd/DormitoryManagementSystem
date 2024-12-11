package server.persistence.dao;

import server.persistence.dto.PaymentCodeDTO;
import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.PaymentMethodDTO;
import server.persistence.dto.PaymentStatusDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO implements PaymentDAOI {

    @Override
    public PaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT p.id, p.payment_amount, p.created_at, p.payment_code_id, p.payment_status_id, p.payment_method_id, " +
                "ps.status_name AS payment_status, pc.payment_code, " +
                "pm.method_name AS method " +
                " FROM payments p " +
                "LEFT JOIN payment_statuses ps ON p.payment_status_id = ps.id " +
                "LEFT JOIN payment_codes pc ON p.payment_code_id = pc.id " +
                "LEFT JOIN payment_methods pm ON p.payment_method_id = pm.id " +
                "WHERE p.id = ?";
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
    public PaymentDTO findByUid(String uid) throws SQLException {
        String query = "SELECT p.id, p.payment_amount, p.created_at, p.payment_code_id, p.payment_status_id, p.payment_method_id, " +
                "ps.status_name AS payment_status, pc.payment_code AS code, " +
                "pm.method_name AS method " +
                "FROM payments p " +
                "LEFT JOIN payment_statuses ps ON p.payment_status_id = ps.id " +
                "LEFT JOIN payment_codes pc ON p.payment_code_id = pc.id " +
                "LEFT JOIN payment_methods pm ON p.payment_method_id = pm.id " +
                "INNER JOIN payment_histories ph ON ph.payment_id = p.id " +
                "INNER JOIN users u ON u.id = ph.user_id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
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
        String query = "SELECT p.id, p.payment_amount, p.created_at, p.payment_code_id, p.payment_status_id, p.payment_method_id, " +
                "ps.status_name AS payment_status, pc.payment_code AS code, " +
                "pm.method_name AS method " +
                "FROM payments p " +
                "LEFT JOIN payment_statuses ps ON p.payment_status_id = ps.id " +
                "LEFT JOIN payment_codes pc ON p.payment_code_id = pc.id " +
                "LEFT JOIN payment_methods pm ON p.payment_method_id = pm.id";

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
    public void statusUpdate(String uid, String paymentStatusName) throws SQLException {
        String query = "SELECT p.id AS payment_id, ps.id AS ps_id FROM payments p " +
                "INNER JOIN payment_histories ph ON ph.payment_id = p.id " +
                "INNER JOIN users u ON u.uid = ph.user_id " +
                "INNER JOIN payment_statuses ps ON ps.status_name = ? " +
                "WHERE u.uid = ?";
        int id;
        int paymentStatusId;
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,paymentStatusName);
            preparedStatement.setString(2, uid);

            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt("payment_id");
            paymentStatusId = resultSet.getInt("ps_id");
        }

        query = "UPDATE payments SET payment_status_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, paymentStatusId);
            preparedStatement.setInt(2, id);
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
        PaymentCodeDTO paymentCodeDTO = PaymentCodeDTO.builder()
                .id(resultSet.getInt("payment_code_id"))
                .paymentCode(resultSet.getString("payment_code"))
                .build();
        PaymentStatusDTO paymentStatusDTO = PaymentStatusDTO.builder()
                .id(resultSet.getInt("payment_status_id"))
                .statusName(resultSet.getString("payment_status"))
                .build();
        PaymentMethodDTO paymentMethodDTO = PaymentMethodDTO.builder()
                .id(resultSet.getInt("payment_method_id"))
                .methodName(resultSet.getString("method_name"))
                .build();

        return PaymentDTO.builder()
                .id(resultSet.getInt("id"))
                .paymentAmount(resultSet.getInt("payment_amount"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .paymentCodeDTO(paymentCodeDTO)
                .paymentStatusDTO(paymentStatusDTO)
                .paymentMethodDTO(paymentMethodDTO)
                .build();
    }
}
