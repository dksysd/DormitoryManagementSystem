package server.persistence.dao;

import server.persistence.dto.GradeLevelDTO;

import java.sql.SQLException;
import java.util.List;

public interface GradeLevelDAOI {
    GradeLevelDTO findById(Integer id) throws SQLException;
    GradeLevelDTO findByName(String name) throws SQLException;
    List<GradeLevelDTO> findAll() throws SQLException;
    void save(GradeLevelDTO gradeLevelDTO) throws SQLException;
    void update(GradeLevelDTO gradeLevelDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
