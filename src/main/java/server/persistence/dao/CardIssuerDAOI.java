package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;

import java.sql.SQLException;

public interface CardIssuerDAOI {
    CardIssuerDTO findById(Integer id) throws SQLException;
    CardIssuerDTO findByName (String name) throws SQLException;
    CardIssuerDTO findByCode (String code) throws SQLException;
}
