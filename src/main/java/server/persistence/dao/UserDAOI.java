package server.persistence.dao;

import server.persistence.dto.UserDTO;

import java.sql.SQLException;

public interface UserDAOI {
    UserDTO findById(Integer id) throws SQLException;
    UserDTO findByUid(Integer userId) throws SQLException;

}
