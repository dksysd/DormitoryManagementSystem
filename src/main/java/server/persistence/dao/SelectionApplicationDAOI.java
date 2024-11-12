package server.persistence.dao;

import server.persistence.dto.SelectionApplicationDTO;

import java.sql.SQLException;

public interface SelectionApplicationDAOI {
    SelectionApplicationDTO findById(Integer id) throws SQLException;
}
