package server.persistence.dao;

import server.persistence.dto.UserTypeDTO;

import java.sql.SQLException;

public interface UserTypeDAOI {
    UserTypeDTO findById(Integer id) throws SQLException;
    UserTypeDTO findByTypeName(String typeName) throws SQLException;
}
