package server.persistence.dao;

import server.persistence.dto.PaymentHistoryDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentHistoryDAOI {
    PaymentHistoryDTO findById(Integer id) throws SQLException;
    List<PaymentHistoryDTO> findAll() throws SQLException;
    void save(PaymentHistoryDTO paymentHistoryDTO) throws SQLException;
    void update(PaymentHistoryDTO paymentHistoryDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
