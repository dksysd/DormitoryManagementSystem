package server.persistence.dao;

import server.persistence.dto.BankTransferPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface BankTransferPaymentDAOI {
    BankTransferPaymentDTO findById(Integer id) throws SQLException;
    public List<BankTransferPaymentDTO> findAll() throws SQLException;
    public void save(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;
    public void update(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;
    public void delete(Integer id) throws SQLException;
}
