package server.persistence.dao;

import server.persistence.dto.PaymentStatusDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentStatusDAOI {
    PaymentStatusDTO findById(Integer id) throws SQLException;
    List<PaymentStatusDTO> findAll() throws SQLException;
    void save(PaymentStatusDTO paymentStatusDTO) throws SQLException;
    void update(PaymentStatusDTO paymentStatusDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
