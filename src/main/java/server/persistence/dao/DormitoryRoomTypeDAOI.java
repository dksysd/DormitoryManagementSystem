package server.persistence.dao;

import server.persistence.dto.DormitoryRoomTypeDTO;

import java.sql.SQLException;

public interface DormitoryRoomTypeDAOI {
    DormitoryRoomTypeDTO findById(Integer id) throws SQLException;
}
