package server.persistence.dao;

import server.persistence.dto.GradeDTO;

import java.sql.SQLException;
import java.util.List;

public interface GradeDAOI {
    GradeDTO findById(Integer id) throws SQLException;
    List<GradeDTO> findAll() throws SQLException;
    void save(GradeDTO gradeDTO) throws SQLException;
    void update(GradeDTO gradeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
