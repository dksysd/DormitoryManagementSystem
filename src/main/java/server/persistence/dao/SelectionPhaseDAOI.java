package server.persistence.dao;

import server.persistence.dto.SelectionPhaseDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionPhaseDAOI {
    SelectionPhaseDTO findById(Integer id) throws SQLException;
    SelectionPhaseDTO findByName(String name) throws SQLException;
    List<SelectionPhaseDTO> findAll() throws SQLException;
    void save(SelectionPhaseDTO selectionPhaseDTO) throws SQLException;
    void update(SelectionPhaseDTO selectionPhaseDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
