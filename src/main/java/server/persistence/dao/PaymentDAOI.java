package server.persistence.dao;

import server.persistence.dto.PaymentDTO;

import java.sql.SQLException;

public interface PaymentDAOI {
    PaymentDTO findById(Integer id) throws SQLException;
}
