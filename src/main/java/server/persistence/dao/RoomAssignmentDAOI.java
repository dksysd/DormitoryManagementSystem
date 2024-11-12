package server.persistence.dao;

import server.persistence.dto.RoomAssignmentDTO;

import java.sql.SQLException;

public interface RoomAssignmentDAOI {
    RoomAssignmentDTO findById(Integer id) throws SQLException;
}
