package server.persistence.dao;

import server.persistence.dto.BankDTO;
import server.persistence.dto.BankTransferPaymentDTO;
import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BankTransferPaymentDAO implements BankTransferPaymentDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(BankTransferPaymentDTO paymentDTO) throws SQLException {
        String query = "UPDATE bank_transfer_payments SET account_number = ?, account_holder_name = ?" +
                "WHERE id = ?";
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

    // update에 필요한 데이터를 각각 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(String uid, String accountNumber, String accountHolderName, String bankName) throws SQLException {
        String query = "SELECT btp.id AS btp_id, b.id AS bank_id FROM bank_transfer_payments btp " +
                "INNER JOIN payments p ON btp.payment_id = p.id " +
                "INNER JOIN payment_histories ph ON ph.payment_id = p.id " +
                "INNER JOIN users u ON u.uid = ph.user_id " +
                "INNER JOIN banks b ON b.bank_name = ?" +
                "WHERE u.uid = ?";
        int btp_id;
        int bank_id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bankName);
            preparedStatement.setString(2,uid);

            ResultSet resultSet = preparedStatement.executeQuery();
            btp_id = resultSet.getInt("btp_id");
            bank_id = resultSet.getInt("bank_id");
        }
        query = "UPDATE bank_transfer_payments btp SET btp.account_number = ?, btp.account_holder_name = ?, btp.bank_id = ?" +
                "WHERE id = " + btp_id;


        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, accountHolderName);
            preparedStatement.setInt(3,bank_id);
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM bank_transfer_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private BankTransferPaymentDTO mapRowToBankTransferPaymentDTO(ResultSet resultSet) throws SQLException {
        BankTransferPaymentDAO dao1 = new BankTransferPaymentDAO();
        BankTransferPaymentDTO paymentDTO = dao1.findById(resultSet.getInt("id"));

        PaymentDAO dao2 = new PaymentDAO();
        // PaymentDTO와 BankDTO 구성
        PaymentDTO payment = dao2.findById(resultSet.getInt("payment_id"));

        BankDAO dao3 = new BankDAO();
        BankDTO bank = dao3.findById(resultSet.getInt("bank_id"));

        return BankTransferPaymentDTO.builder()
                .accountHolderName(resultSet.getString("account_holder_name"))
                .bankDTO(bank)
                .paymentDTO(payment)
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .accountNumber(resultSet.getString("account_number"))
                .build();
    }
}
