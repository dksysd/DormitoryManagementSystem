package server.persistence.dao;

import server.persistence.dto.BankDTO;

import java.sql.SQLException;

public interface BankDAOI {
    BankDTO findById(Integer id) throws SQLException;
    BankDTO findByName(String name) throws SQLException;
    BankDTO findByCode(String code) throws SQLException;
}
