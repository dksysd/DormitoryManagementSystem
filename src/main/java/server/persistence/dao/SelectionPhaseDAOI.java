package server.persistence.dao;

import server.persistence.dto.SelectionPhaseDTO;

import java.sql.SQLException;

public interface SelectionPhaseDAOI {
    SelectionPhaseDTO findById(Integer id) throws SQLException;
    SelectionPhaseDTO findByPhaseName(String phaseName) throws SQLException;
}
