package server.persistence.dao;

import server.persistence.dto.MoveOutRequestDTO;

import java.sql.SQLException;

public interface MoveOutRequestStatusDAOI {
    MoveOutRequestDTO findById(Integer id) throws SQLException;
}
