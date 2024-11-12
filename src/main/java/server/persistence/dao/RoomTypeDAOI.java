package server.persistence.dao;

import server.persistence.dto.RoomTypeDTO;

import java.sql.SQLException;

public interface RoomTypeDAOI {
    RoomTypeDTO findById(Integer id) throws SQLException;
}
