package server.persistence.dao;

import server.persistence.dto.GradeLevelDTO;

import java.sql.SQLException;

public interface GradeLevelDAOI {
    GradeLevelDTO findById(Integer id) throws SQLException;
    GradeLevelDTO findByLevelName(String levelName) throws SQLException;
}
