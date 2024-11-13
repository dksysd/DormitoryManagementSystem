package server.persistence.dao;

import server.persistence.dto.DemeritPointDTO;

import java.sql.SQLException;
import java.util.List;

public interface DemeritPointDAOI {
    DemeritPointDTO findById(Integer id) throws SQLException;
    List<DemeritPointDTO> findAll() throws SQLException;
    void save(DemeritPointDTO demeritPointDTO) throws SQLException;
    void update(DemeritPointDTO demeritPointDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
