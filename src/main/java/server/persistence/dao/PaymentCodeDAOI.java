package server.persistence.dao;

import server.persistence.dto.PaymentCodeDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentCodeDAOI {
    PaymentCodeDTO findById(Integer id) throws SQLException;
    PaymentCodeDTO findByCode(String code) throws SQLException;
    List<PaymentCodeDTO> findAll() throws SQLException;
    void save(PaymentCodeDTO paymentCodeDTO) throws SQLException;
    void update(PaymentCodeDTO paymentCodeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
