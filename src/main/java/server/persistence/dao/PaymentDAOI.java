package server.persistence.dao;
import server.persistence.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDAOI {
    PaymentDTO findById(Integer id) throws SQLException;
    List<PaymentDTO> findAll() throws SQLException;
    PaymentDTO findByUid(String uid) throws SQLException;
    void save(PaymentDTO paymentDTO) throws SQLException;
    void update(PaymentDTO paymentDTO) throws SQLException;
    void statusUpdate(String uid, String paymentStatusName) throws SQLException;
    void delete(Integer id) throws SQLException;
}
