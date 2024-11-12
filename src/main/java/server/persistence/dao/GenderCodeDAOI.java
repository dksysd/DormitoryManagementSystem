package server.persistence.dao;

import server.persistence.dto.GenderCodeDTO;

import java.sql.SQLException;

public interface GenderCodeDAOI {
    GenderCodeDTO findById(Integer id) throws SQLException;
    GenderCodeDTO findByCodeName(String codeName) throws SQLException;
}
