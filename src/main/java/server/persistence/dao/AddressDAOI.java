package server.persistence.dao;

import server.persistence.dto.AddressDTO;

import java.sql.SQLException;

public interface AddressDAOI {
    AddressDTO findById(Integer id) throws SQLException;
}
