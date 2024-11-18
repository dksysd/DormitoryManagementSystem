package server.persistence.dao;

import server.persistence.dto.AddressDTO;

import java.sql.SQLException;
import java.util.List;

public interface AddressDAOI {
    AddressDTO findById(Integer id) throws SQLException;
    List<AddressDTO> findAll() throws SQLException;
    void save(AddressDTO addressDTO) throws SQLException;
    void update(AddressDTO addressDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}