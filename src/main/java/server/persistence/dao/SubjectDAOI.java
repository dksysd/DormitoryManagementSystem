package server.persistence.dao;

import server.persistence.dto.SubjectDTO;

import java.sql.SQLException;
import java.util.List;

public interface SubjectDAOI {
    SubjectDTO findById(Integer id) throws SQLException;
    SubjectDTO findByName(String name) throws SQLException;
    List<SubjectDTO> findAll() throws SQLException;
    void save(SubjectDTO subjectDTO) throws SQLException;
    void update(SubjectDTO subjectDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
