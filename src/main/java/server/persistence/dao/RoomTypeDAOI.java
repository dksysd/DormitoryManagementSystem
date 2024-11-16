package server.persistence.dao;

import server.persistence.dto.RoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoomTypeDAOI {
    RoomTypeDTO findById(Integer id) throws SQLException;
    List<RoomTypeDTO> findAll() throws SQLException;
    void save(RoomTypeDTO roomTypeDTO) throws SQLException;
    void update(RoomTypeDTO roomTypeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
