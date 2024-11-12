package server.persistence.dao;

import server.persistence.dto.GradeDTO;

import java.sql.SQLException;

public interface GradeDAOI {
    GradeDTO findById(Integer id) throws SQLException;
}
