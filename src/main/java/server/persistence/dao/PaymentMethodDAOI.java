package server.persistence.dao;

import server.persistence.dto.PaymentMethodDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentMethodDAOI {
    PaymentMethodDTO findById(Integer id) throws SQLException;
    PaymentMethodDTO findByName(String name) throws SQLException;
    List<PaymentMethodDTO> findAll() throws SQLException;
    void save(PaymentMethodDTO PaymentMethodDTO) throws SQLException;
    void update(PaymentMethodDTO PaymentMethodDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
