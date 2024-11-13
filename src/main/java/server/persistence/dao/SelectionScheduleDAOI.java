package server.persistence.dao;

import server.persistence.dto.SelectionScheduleDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionScheduleDAOI {
    SelectionScheduleDTO findById(Integer id) throws SQLException;
    List<SelectionScheduleDTO> findAll() throws SQLException;
    void save(SelectionScheduleDTO selectionScheduleDTO) throws SQLException;
    void update(SelectionScheduleDTO selectionScheduleDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
