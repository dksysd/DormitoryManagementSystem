package server.persistence.dao;

import server.persistence.dto.BankDTO;
import server.persistence.dto.PaymentDTO;
import server.persistence.dto.PaymentRefundDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRefundDAO implements PaymentRefundDAOI {

    @Override
    public PaymentRefundDTO findById(Integer id) throws SQLException {
        String query = "SELECT pr.id AS refund_id, pr.refund_reason, pr.account_number, pr.account_holder_name, " +
                "pr.created_at, b.id AS bank_id, b.bank_name, b.bank_code, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM payment_refunds pr " +
                "LEFT JOIN banks b ON pr.bank_id = b.id " +
                "LEFT JOIN payments p ON pr.payment_id = p.id " +
                "WHERE pr.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentRefundDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<PaymentRefundDTO> findAll() throws SQLException {
        List<PaymentRefundDTO> paymentRefunds = new ArrayList<>();
        String query = "SELECT pr.id AS refund_id, pr.refund_reason, pr.account_number, pr.account_holder_name, " +
                "pr.created_at, b.id AS bank_id, b.bank_name, b.bank_code, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM payment_refunds pr " +
                "LEFT JOIN banks b ON pr.bank_id = b.id " +
                "LEFT JOIN payments p ON pr.payment_id = p.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                paymentRefunds.add(mapRowToPaymentRefundDTO(resultSet));
            }
        }
        return paymentRefunds; // 모든 결제 환불 정보 반환
    }

    @Override
    public void save(PaymentRefundDTO paymentRefundDTO) throws SQLException {
        String query = "INSERT INTO payment_refunds (refund_reason, account_number, account_holder_name, created_at, bank_id, payment_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentRefundDTO.getRefundReason());
            preparedStatement.setString(2, paymentRefundDTO.getAccountNumber());
            preparedStatement.setString(3, paymentRefundDTO.getAccountHolderName());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(paymentRefundDTO.getCreatedAt()));
            preparedStatement.setInt(5, paymentRefundDTO.getBankDTO() != null ? paymentRefundDTO.getBankDTO().getId() : null);
            preparedStatement.setInt(6, paymentRefundDTO.getPaymentDTO() != null ? paymentRefundDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentRefundDTO paymentRefundDTO) throws SQLException {
        String query = "UPDATE payment_refunds SET refund_reason = ?, account_number = ?, account_holder_name = ?, created_at = ?, bank_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentRefundDTO.getRefundReason());
            preparedStatement.setString(2, paymentRefundDTO.getAccountNumber());
            preparedStatement.setString(3, paymentRefundDTO.getAccountHolderName());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(paymentRefundDTO.getCreatedAt()));
            preparedStatement.setInt(5, paymentRefundDTO.getBankDTO() != null ? paymentRefundDTO.getBankDTO().getId() : null);
            preparedStatement.setInt(6, paymentRefundDTO.getPaymentDTO() != null ? paymentRefundDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(7, paymentRefundDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_refunds WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentRefundDTO mapRowToPaymentRefundDTO(ResultSet resultSet) throws SQLException {
        BankDTO bankDTO = BankDTO.builder()
                .id(resultSet.getInt("bank_id"))
                .bankName(resultSet.getString("bank_name"))
                .bankCode(resultSet.getString("bank_code"))
                .build();

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .id(resultSet.getInt("payment_id"))
                .paymentAmount(resultSet.getInt("payment_amount"))
                .build();

        return PaymentRefundDTO.builder()
                .id(resultSet.getInt("refund_id"))
                .refundReason(resultSet.getString("refund_reason"))
                .accountNumber(resultSet.getString("account_number"))
                .accountHolderName(resultSet.getString("account_holder_name"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .bankDTO(bankDTO) // BankDTO 정보 추가
                .paymentDTO(paymentDTO) // PaymentDTO 정보 추가
                .build();
    }
}
