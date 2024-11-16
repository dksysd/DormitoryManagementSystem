package server.persistence.dao;

import server.persistence.dto.GenderCodeDTO;

import java.sql.SQLException;
import java.util.List;

public interface GenderCodeDAOI {
    GenderCodeDTO findById(Integer id) throws SQLException;
    GenderCodeDTO findByName(String name) throws SQLException;
    List<GenderCodeDTO> findAll() throws SQLException;
    void save(GenderCodeDTO genderCodeDTO) throws SQLException;
    void update(GenderCodeDTO genderCodeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
