package server.persistence.dao;

import server.persistence.dto.CardPaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface CardPaymentDAOI {
    CardPaymentDTO findById(Integer id) throws SQLException;
    CardPaymentDTO findByCardNumber(String cardNumber) throws SQLException;
    List<CardPaymentDTO> findAll() throws SQLException;
    void save(CardPaymentDTO cardPaymentDTO) throws SQLException;
    void update(CardPaymentDTO cardPaymentDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
