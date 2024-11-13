package server.persistence.dao;

import server.persistence.dto.PaymentStatusDTO;

import java.sql.SQLException;

public interface PaymentStatusDAOI {
    PaymentStatusDTO findById(Integer id) throws SQLException;
    PaymentStatusDTO findByPaymentName(String paymentName) throws SQLException;
}
