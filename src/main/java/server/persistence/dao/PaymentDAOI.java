package server.persistence.dao;
//todo id로 내가 내야하는 금액 뱉는 메서드 만들어주세요
import server.persistence.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDAOI {
    PaymentDTO findById(Integer id) throws SQLException;
    List<PaymentDTO> findAll() throws SQLException;
    void save(PaymentDTO paymentDTO) throws SQLException;
    void update(PaymentDTO paymentDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
