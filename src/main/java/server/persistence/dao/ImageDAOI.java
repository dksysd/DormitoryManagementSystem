package server.persistence.dao;

import server.persistence.dto.ImageDTO;

import java.sql.SQLException;

public interface ImageDAOI {
    ImageDTO findById(Integer id) throws SQLException;
}
