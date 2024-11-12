package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.CardPaymentDTO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CardPaymentDAO implements CardPaymentDAOI {
    @Override
    public CardPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT * FROM card_payments WHERE id = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer cardPaymentId = rs.getInt("id");
        String cardNumber = rs.getString("card_number");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return CardPaymentDTO.builder()
                .id(cardPaymentId)
                .cardNumber(cardNumber)
                .createdAt(createdAt)
                .build();
    }

    @Override
    public CardPaymentDTO findByNumber(String number) throws SQLException {
        String query = "SELECT * FROM card_payments WHERE number = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, number);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer cardPaymentId = rs.getInt("id");
        String cardNumber = rs.getString("card_number");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return
    }
}
