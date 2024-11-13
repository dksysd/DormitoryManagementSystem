package server.persistence.dao;

import server.persistence.dto.PaymentRefundDTO;

import java.sql.SQLException;

public interface PaymentRefundDAOI {
    PaymentRefundDTO findById(Integer id) throws SQLException;
}
