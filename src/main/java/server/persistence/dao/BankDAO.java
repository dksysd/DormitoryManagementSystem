package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.BankDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<BankDTO> findAll() throws SQLException {
        List<BankDTO> banks = new ArrayList<BankDTO>();
        String query = "SELECT * FROM banks";
        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                banks.add(mapRowToBank(rs));
            }
        }

        return banks;
    }

    @Override
    public void save(BankDTO bank) throws SQLException {
        String query = "INSERT INTO banks (bank_name, bank_code) VALUES (?, ?)";
        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, bank.getBankName());
            ps.setString(2, bank.getBankCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(BankDTO bank) throws SQLException {
        String query = "UPDATE banks SET bank_name = ?, bank_code = ? WHERE id = ?";
        try(Connection conn = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, bank.getBankName());
            ps.setString(2, bank.getBankCode());
            ps.setInt(3, bank.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM banks WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }
    }

    private BankDTO mapRowToBank(ResultSet rs) throws SQLException {
        BankDTO bankDTO = BankDTO.builder()
                .id(rs.getInt("id"))
                .bankName(rs.getString("bank_name"))
                .bankCode(rs.getString("bank_code"))
                .build();

        return bankDTO;
    }
}
