package server.persistence.dao;

import server.persistence.dto.SelectionPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionPaymentDAOI {
    SelectionPaymentDTO findById(Integer id) throws SQLException;
    List<SelectionPaymentDTO> findAll() throws SQLException;
    void save(SelectionPaymentDTO selectionPaymentDTO) throws SQLException;
    void update(SelectionPaymentDTO selectionPaymentDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
