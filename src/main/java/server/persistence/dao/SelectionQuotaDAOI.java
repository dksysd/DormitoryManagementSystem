package server.persistence.dao;

import server.persistence.dto.SelectionQuotaDTO;

import java.sql.SQLException;

public interface SelectionQuotaDAOI {
    SelectionQuotaDTO findById(Integer id) throws SQLException;
}
