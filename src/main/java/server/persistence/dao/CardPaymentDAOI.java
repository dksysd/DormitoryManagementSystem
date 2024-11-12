package server.persistence.dao;

import server.persistence.dto.CardPaymentDTO;

import java.sql.SQLException;

public interface CardPaymentDAOI {
    CardPaymentDTO findById(Integer id) throws SQLException;
    CardPaymentDTO findByNumber(String number) throws SQLException;
}
