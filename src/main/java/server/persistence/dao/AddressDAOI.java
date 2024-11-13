package server.persistence.dao;

import server.persistence.dto.AddressDTO;

import java.sql.SQLException;
import java.util.List;

public interface AddressDAOI {
    AddressDTO findById(Integer id) throws SQLException;
    public List<AddressDTO> findAll() throws SQLException;
    public void save(AddressDTO addressDTO) throws SQLException;
    public void update(AddressDTO addressDTO) throws SQLException;
    public void delete(Integer id) throws SQLException;
}