package server.persistence.dao;

import server.persistence.dto.SubjectDTO;

import java.sql.SQLException;

public interface SubjectDAOI {
    SubjectDTO findById(Integer id) throws SQLException;
    SubjectDTO findBySubjectName(String subjectName) throws SQLException;
}
