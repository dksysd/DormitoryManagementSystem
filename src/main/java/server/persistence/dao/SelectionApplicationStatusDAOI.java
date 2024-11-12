package server.persistence.dao;

import server.persistence.dto.SelectionApplicationStatusDTO;

import java.sql.SQLException;

public interface SelectionApplicationStatusDAOI {
    SelectionApplicationStatusDTO findById(Integer id) throws SQLException;
    SelectionApplicationStatusDTO findByStatusName(String statusName) throws SQLException;
}
