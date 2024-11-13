package server.persistence.dao;

import server.persistence.dto.AddressDTO;
import server.persistence.dto.BankDTO;

import java.sql.SQLException;
import java.util.List;

public interface BankDAOI {
    BankDTO findById(Integer id) throws SQLException;
    BankDTO findByName(String name) throws SQLException;
    BankDTO findByCode(String code) throws SQLException;
    public List<BankDTO> findAll() throws SQLException;
    public void save(BankDTO bankDTO) throws SQLException;
    public void update(BankDTO bankDTO) throws SQLException;
    public void delete(Integer id) throws SQLException;
}
