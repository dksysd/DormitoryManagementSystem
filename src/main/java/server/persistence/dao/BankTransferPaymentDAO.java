package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.BankTransferPaymentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BankTransferPaymentDAO implements BankTransferPaymentDAOI {
    @Override
    public BankTransferPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT * FROM bank_transfer_payments WHERE id = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return null;
        }

        Integer bankTransferPaymentId = rs.getInt("id");
        String accountNumber = rs.getString("account_number");
        String accountHolderName = rs.getString("account_holder_name");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        Integer paymentId = rs.getInt("payment_id");
        Integer bankId = rs.getInt("bank_id");

        return BankTransferPaymentDTO.builder()
                .id(bankTransferPaymentId)
                .accountNumber(accountNumber)
                .accountHolderName(accountHolderName)
                .createdAt(createdAt)
                .paymentDTO // how ???
                .build();
    }
}
