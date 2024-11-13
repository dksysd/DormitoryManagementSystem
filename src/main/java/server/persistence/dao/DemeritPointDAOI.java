package server.persistence.dao;

import server.persistence.dto.DemeritPointDTO;

import java.sql.SQLException;

public interface DemeritPointDAOI {
    DemeritPointDTO findById(Integer id) throws SQLException;
}
