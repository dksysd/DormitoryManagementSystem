package server.persistence.dao;

import server.persistence.dto.UserTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserTypeDAOI {
    UserTypeDTO findById(Integer id) throws SQLException;
    UserTypeDTO findByTypeName(String typeName) throws SQLException;
    List<UserTypeDTO> findAll() throws SQLException;
    void save(UserTypeDTO userTypeDTO) throws SQLException;
    void update(UserTypeDTO userTypeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
