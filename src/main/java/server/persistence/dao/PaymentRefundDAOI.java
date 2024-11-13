package server.persistence.dao;

import server.persistence.dto.PaymentRefundDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentRefundDAOI {
    PaymentRefundDTO findById(Integer id) throws SQLException;
    List<PaymentRefundDTO> findAll() throws SQLException;
    void save(PaymentRefundDTO paymentRefundDTO) throws SQLException;
    void update(PaymentRefundDTO paymentRefundDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
