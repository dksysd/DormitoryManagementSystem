package server.persistence.dao;

import server.persistence.dto.SelectionApplicationStatusDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionApplicationStatusDAOI {
    SelectionApplicationStatusDTO findById(Integer id) throws SQLException;
    SelectionApplicationStatusDTO findByName(String name) throws SQLException;
    List<SelectionApplicationStatusDTO> findAll() throws SQLException;
    void save(SelectionApplicationStatusDTO selectionApplicationStatusDTO) throws SQLException;
    void update(SelectionApplicationStatusDTO selectionApplicationStatusDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
