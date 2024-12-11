package server.persistence.dao;

import server.persistence.dto.RoomDTO;
import server.persistence.dto.RoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface RoomDAOI {
    RoomDTO findById(Integer id) throws SQLException;
    List<RoomDTO> findAll() throws SQLException;
    void save(RoomDTO roomDTO) throws SQLException;
    void update(RoomDTO roomDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}