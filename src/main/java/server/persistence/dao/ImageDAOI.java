package server.persistence.dao;

import server.persistence.dto.ImageDTO;

import java.sql.SQLException;
import java.util.List;

public interface ImageDAOI {
    ImageDTO findById(Integer id) throws SQLException;
    List<ImageDTO> findAll() throws SQLException;
    int save(ImageDTO imageDTO) throws SQLException;
    void update(ImageDTO imageDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
