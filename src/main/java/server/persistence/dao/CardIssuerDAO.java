package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardIssuerDAO implements CardIssuerDAOI {

    @Override
    public CardIssuerDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, issuer_name, issuer_code FROM card_issuers WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardIssuerDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public CardIssuerDTO findByName(String issuerName) throws SQLException {
        String query = "SELECT id, issuer_name, issuer_code FROM card_issuers WHERE issuer_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, issuerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardIssuerDTO(resultSet);
            }
        }
        return null; // 이름에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public CardIssuerDTO findByCode(String issuerCode) throws SQLException {
        String query = "SELECT id, issuer_name, issuer_code FROM card_issuers WHERE issuer_code = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, issuerCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardIssuerDTO(resultSet);
            }
        }
        return null; // 코드에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<CardIssuerDTO> findAll() throws SQLException {
        List<CardIssuerDTO> issuers = new ArrayList<>();
        String query = "SELECT id, issuer_name, issuer_code FROM card_issuers";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                issuers.add(mapRowToCardIssuerDTO(resultSet));
            }
        }
        return issuers; // 모든 카드 발급자 정보 반환
    }

    @Override
    public void save(CardIssuerDTO cardIssuerDTO) throws SQLException {
        String query = "INSERT INTO card_issuers (issuer_name, issuer_code) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardIssuerDTO.getIssuerName());
            preparedStatement.setString(2, cardIssuerDTO.getIssuerCode());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(CardIssuerDTO cardIssuerDTO) throws SQLException {
        String query = "UPDATE card_issuers SET issuer_name = ?, issuer_code = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardIssuerDTO.getIssuerName());
            preparedStatement.setString(2, cardIssuerDTO.getIssuerCode());
            preparedStatement.setInt(3, cardIssuerDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM card_issuers WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private CardIssuerDTO mapRowToCardIssuerDTO(ResultSet resultSet) throws SQLException {
        return CardIssuerDTO.builder()
                .id(resultSet.getInt("id"))
                .issuerName(resultSet.getString("issuer_name"))
                .issuerCode(resultSet.getString("issuer_code"))
                .build();
    }
}
