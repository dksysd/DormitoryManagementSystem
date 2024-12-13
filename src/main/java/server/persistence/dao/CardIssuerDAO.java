package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardIssuerDAO implements CardIssuerDAOI {

    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // 이름을 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // 코드를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM card_issuers WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private CardIssuerDTO mapRowToCardIssuerDTO(ResultSet resultSet) throws SQLException {
        return CardIssuerDTO.builder()
                .id(resultSet.getInt("id"))
                .issuerName(resultSet.getString("issuer_name"))
                .issuerCode(resultSet.getString("issuer_code"))
                .build();
    }
}
