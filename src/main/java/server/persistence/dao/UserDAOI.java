package server.persistence.dao;

import server.persistence.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserDAOI {
    UserDTO findById(Integer id) throws SQLException;
    UserDTO findByUid(String uid) throws SQLException;
    List<UserDTO> findAll() throws SQLException;
    void save(UserDTO userDTO) throws SQLException;
    void update(UserDTO userDTO) throws SQLException;
    void updateRoommate(String uid, int roommate) throws SQLException;
    void delete(Integer id) throws SQLException;
}
