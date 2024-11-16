package server.persistence.dao;

import server.persistence.dto.MoveOutRequestDTO;

import java.sql.SQLException;
import java.util.List;

public interface MoveOutRequestDAOI {
    MoveOutRequestDTO findById(Integer id) throws SQLException;
    List<MoveOutRequestDTO> findAll() throws SQLException;
    void save(MoveOutRequestDTO moveOutRequestDTO) throws SQLException;
    void update(MoveOutRequestDTO moveOutRequestDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
