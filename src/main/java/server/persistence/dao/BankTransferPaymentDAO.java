package server.persistence.dao;

import server.persistence.dto.BankDTO;
import server.persistence.dto.BankTransferPaymentDTO;
import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankTransferPaymentDAO implements BankTransferPaymentDAOI {

    @Override
    public BankTransferPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT btp.id, btp.account_number, btp.account_holder_name, btp.created_at, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at, " +
                "b.id AS bank_id, b.bank_name, b.bank_code " +
                "FROM bank_transfer_payments btp " +
                "LEFT JOIN payments p ON btp.payment_id = p.id " +
                "LEFT JOIN banks b ON btp.bank_id = b.id " +
                "WHERE btp.id = ?";

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapRowToBankTransferPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<BankTransferPaymentDTO> findAll() throws SQLException {
        List<BankTransferPaymentDTO> payments = new ArrayList<>();
        String query = "SELECT btp.id, btp.account_number, btp.account_holder_name, btp.created_at, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at, " +
                "b.id AS bank_id, b.bank_name, b.bank_code " +
                "FROM bank_transfer_payments btp " +
                "LEFT JOIN payments p ON btp.payment_id = p.id " +
                "LEFT JOIN banks b ON btp.bank_id = b.id";

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                payments.add(mapRowToBankTransferPaymentDTO(resultSet));
            }
        }
        return payments; // 모든 결제 정보 반환
    }

    @Override
    public void save(BankTransferPaymentDTO paymentDTO) throws SQLException {
        String query = "INSERT INTO bank_transfer_payments (account_number, account_holder_name, created_at, payment_id, bank_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentDTO.getAccountNumber());
            preparedStatement.setString(2, paymentDTO.getAccountHolderName());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(paymentDTO.getCreatedAt()));
            preparedStatement.setInt(4, paymentDTO.getPaymentDTO() != null ? paymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(5, paymentDTO.getBankDTO() != null ? paymentDTO.getBankDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(BankTransferPaymentDTO paymentDTO) throws SQLException {
        String query = "UPDATE bank_transfer_payments SET account_number = ?, account_holder_name = ?, created_at = ?, payment_id = ?, bank_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentDTO.getAccountNumber());
            preparedStatement.setString(2, paymentDTO.getAccountHolderName());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(paymentDTO.getCreatedAt()));
            preparedStatement.setInt(4, paymentDTO.getPaymentDTO() != null ? paymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(5, paymentDTO.getBankDTO() != null ? paymentDTO.getBankDTO().getId() : null);
            preparedStatement.setInt(6, paymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM bank_transfer_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private BankTransferPaymentDTO mapRowToBankTransferPaymentDTO(ResultSet resultSet) throws SQLException {
        BankTransferPaymentDTO paymentDTO = BankTransferPaymentDTO.builder()
                .id(resultSet.getInt("id"))
                .accountNumber(resultSet.getString("account_number"))
                .accountHolderName(resultSet.getString("account_holder_name"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();

        // PaymentDTO와 BankDTO 구성
        PaymentDTO payment = PaymentDTO.builder()
                .id(resultSet.getInt("payment_id"))
                .paymentAmount(resultSet.getInt("payment_amount"))
                .createdAt(resultSet.getTimestamp("payment_created_at").toLocalDateTime())
                .build();

        BankDTO bank = BankDTO.builder()
                .id(resultSet.getInt("bank_id"))
                .bankName(resultSet.getString("bank_name"))
                .bankCode(resultSet.getString("bank_code"))
                .build();

        paymentDTO.setPaymentDTO(payment);
        paymentDTO.setBankDTO(bank);

        return paymentDTO;
    }
}
