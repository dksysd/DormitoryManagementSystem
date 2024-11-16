package server.persistence.dao;

import server.persistence.dto.SelectionQuotaDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionQuotaDAOI {
    SelectionQuotaDTO findById(Integer id) throws SQLException;
    List<SelectionQuotaDTO> findAll() throws SQLException;
    void save(SelectionQuotaDTO selectionQuotaDTO) throws SQLException;
    void update(SelectionQuotaDTO selectionQuotaDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
