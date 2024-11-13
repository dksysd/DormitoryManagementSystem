package server.persistence.dao;

import server.persistence.dto.SelectionDTO;

import java.sql.SQLException;

public interface SelectionDAOI {
    SelectionDTO findById(Integer id) throws SQLException;
}
