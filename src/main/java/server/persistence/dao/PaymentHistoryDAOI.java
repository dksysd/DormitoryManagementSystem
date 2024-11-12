package server.persistence.dao;

import server.persistence.dto.PaymentHistoryDTO;

import java.sql.SQLException;

public interface PaymentHistoryDAOI {
    PaymentHistoryDTO findById(Integer id) throws SQLException;
}
