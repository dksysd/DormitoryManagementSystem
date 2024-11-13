package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.CardIssuerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardIssuerDAO implements CardIssuerDAOI {
    @Override
    public CardIssuerDTO findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM card_issuers WHERE id = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer cardIssuerId = rs.getInt("id");
        String issuerName = rs.getString("issuer_name");
        String issuerCode = rs.getString("issuer_code");

        return CardIssuerDTO.builder()
                .id(cardIssuerId)
                .issuerName(issuerName)
                .issuerCode(issuerCode)
                .build();
    }

    @Override
    public CardIssuerDTO findByName(String name) throws SQLException {
        String sql = "SELECT * FROM card_issuers WHERE issuer_name = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, issuerName);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer cardIssuerId = rs.getInt("id");
        String issuerName = rs.getString("issuer_name");
        String issuerCode = rs.getString("issuer_code");

        return CardIssuerDTO.builder()
                .id(cardIssuerId)
                .issuerName(issuerName)
                .issuerCode(issuerCode)
                .build();
    }

    @Override
    public CardIssuerDTO findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM card_issuers WHERE code = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer cardIssuerId = rs.getInt("id");
        String issuerName = rs.getString("issuer_name");
        String issuerCode = rs.getString("issuer_code");

        return CardIssuerDTO.builder()
                .id(cardIssuerId)
                .issuerName(issuerName)
                .issuerCode(issuerCode)
                .build();
    }
}
