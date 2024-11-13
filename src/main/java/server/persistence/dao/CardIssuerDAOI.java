package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CardIssuerDAOI {
    CardIssuerDTO findById(Integer id) throws SQLException;
    CardIssuerDTO findByName(String issuerName) throws SQLException;
    CardIssuerDTO findByCode(String issuerCode) throws SQLException;
    List<CardIssuerDTO> findAll() throws SQLException;
    void save(CardIssuerDTO cardIssuerDTO) throws SQLException;
    void update(CardIssuerDTO cardIssuerDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
