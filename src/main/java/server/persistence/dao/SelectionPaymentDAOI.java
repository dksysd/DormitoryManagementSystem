package server.persistence.dao;

import server.persistence.dto.SelectionPaymentDTO;

import java.sql.SQLException;

public interface SelectionPaymentDAOI {
    SelectionPaymentDTO findById(Integer id) throws SQLException;
}
