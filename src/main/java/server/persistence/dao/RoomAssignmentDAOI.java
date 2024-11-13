package server.persistence.dao;

import server.persistence.dto.RoomAssignmentDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoomAssignmentDAOI {
    RoomAssignmentDTO findById(Integer id) throws SQLException;
    List<RoomAssignmentDTO> findAll() throws SQLException;
    void save(RoomAssignmentDTO roomAssignmentDTO) throws SQLException;
    void update(RoomAssignmentDTO roomAssignmentDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
