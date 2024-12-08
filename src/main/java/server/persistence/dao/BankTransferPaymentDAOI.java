package server.persistence.dao;

import server.persistence.dto.BankTransferPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface BankTransferPaymentDAOI {
    BankTransferPaymentDTO findById(Integer id) throws SQLException;
    List<BankTransferPaymentDTO> findAll() throws SQLException;
    void save(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;
    void update(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;
    void update(String uid, String accountNumber, String accountHolderName, String bankName) throws SQLException;
    void delete(Integer id) throws SQLException;
}
