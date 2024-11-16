package server.persistence.dao;

import server.persistence.dto.MoveOutRequestStatusDTO;

import java.sql.SQLException;
import java.util.List;

public interface MoveOutRequestStatusDAOI {
    MoveOutRequestStatusDTO findById(Integer id) throws SQLException;
    List<MoveOutRequestStatusDTO> findAll() throws SQLException;
    void save(MoveOutRequestStatusDTO moveOutRequestStatusDTO) throws SQLException;
    void update(MoveOutRequestStatusDTO moveOutRequestStatusDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
