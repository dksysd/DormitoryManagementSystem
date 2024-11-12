package server.persistence.dao;

import server.persistence.dto.BankTransferPaymentDTO;

import java.sql.SQLException;

public interface BankTransferPaymentDAOI {
    BankTransferPaymentDTO findById(Integer id) throws SQLException;
}
