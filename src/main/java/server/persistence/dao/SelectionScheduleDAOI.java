package server.persistence.dao;

import server.persistence.dto.SelectionScheduleDTO;

import java.sql.SQLException;

public interface SelectionScheduleDAOI {
    SelectionScheduleDTO findById(Integer id) throws SQLException;
}
