package server.persistence.dao;

import server.persistence.dto.CardPaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentDAO implements CardPaymentDAOI {

    @Override
    public CardPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public CardPaymentDTO findByCardNumber(String cardNumber) throws SQLException {
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments WHERE card_number = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardPaymentDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<CardPaymentDTO> findAll() throws SQLException {
        List<CardPaymentDTO> payments = new ArrayList<>();
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                payments.add(mapRowToCardPaymentDTO(resultSet));
            }
        }
        return payments; // 모든 카드 결제 정보 반환
    }

    @Override
    public void save(CardPaymentDTO cardPaymentDTO) throws SQLException {
        String query = "INSERT INTO card_payments (card_number, created_at, card_issuer_id, payment_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardPaymentDTO.getCardNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(cardPaymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, cardPaymentDTO.getCardIssuerDTO() != null ? cardPaymentDTO.getCardIssuerDTO().getId() : null);
            preparedStatement.setInt(4, cardPaymentDTO.getPaymentDTO() != null ? cardPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(CardPaymentDTO cardPaymentDTO) throws SQLException {
        String query = "UPDATE card_payments SET card_number = ?, created_at = ?, card_issuer_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardPaymentDTO.getCardNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(cardPaymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, cardPaymentDTO.getCardIssuerDTO() != null ? cardPaymentDTO.getCardIssuerDTO().getId() : null);
            preparedStatement.setInt(4, cardPaymentDTO.getPaymentDTO() != null ? cardPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(5, cardPaymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM card_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private CardPaymentDTO mapRowToCardPaymentDTO(ResultSet resultSet) throws SQLException {
        return CardPaymentDTO.builder()
                .id(resultSet.getInt("id"))
                .cardNumber(resultSet.getString("card_number"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
