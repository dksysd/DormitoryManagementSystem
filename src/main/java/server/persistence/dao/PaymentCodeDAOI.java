package server.persistence.dao;

import server.persistence.dto.PaymentCodeDTO;

import java.sql.SQLException;

public interface PaymentCodeDAOI {
    PaymentCodeDTO findById(Integer id) throws SQLException;
    PaymentCodeDTO findByPaymentCode(String paymentCode) throws SQLException;
}
