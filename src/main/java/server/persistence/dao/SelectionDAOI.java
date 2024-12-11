package server.persistence.dao;

import server.persistence.dto.SelectionDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionDAOI {
    SelectionDTO findById(Integer id) throws SQLException;
    public SelectionDTO findByUid(String uid) throws SQLException;
    List<SelectionDTO> findAll() throws SQLException;
    void save(SelectionDTO selectionDTO) throws SQLException;
    void update(SelectionDTO selectionDTO) throws SQLException;
    void updateTuber(String uid, Byte[] data) throws SQLException;
    void updateProof(String uid, Byte[] data) throws SQLException;
    void delete(Integer id) throws SQLException;
}
