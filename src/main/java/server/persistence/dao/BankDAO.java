package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.BankDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDAO implements BankDAOI{
    @Override
    public BankDTO findById(Integer id) throws SQLException {
        String query = "SELECT * FROM banks WHERE id = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return null;
        }

        Integer bankId = rs.getInt("id");
        String bankName = rs.getString("bank_name");
        String bankCode = rs.getString("bank_code");

        return BankDTO.builder()
                .id(bankId)
                .bankName(bankName)
                .bankCode(bankCode)
                .build();
    }

    @Override
    public BankDTO findByName(String name) throws SQLException {
        String query = "SELECT * FROM banks WHERE bank_name = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return null;
        }

        Integer bankId = rs.getInt("id");
        String bankName = rs.getString("bank_name");
        String bankCode = rs.getString("bank_code");

        return BankDTO.builder()
                .id(bankId)
                .bankName(bankName)
                .bankCode(bankCode)
                .build();
    }

    @Override
    public BankDTO findByCode(String code) throws SQLException {
        String query = "SELECT * FROM banks WHERE bank_code = ?";
        Connection conn = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, code);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
            return null;
        }

        Integer bankId = rs.getInt("id");
        String bankName = rs.getString("bank_name");
        String bankCode = rs.getString("bank_code");

        return BankDTO.builder()
                .id(bankId)
                .bankName(bankName)
                .bankCode(bankCode)
                .build();
    }
}
