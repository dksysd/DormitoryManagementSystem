package server.persistence.dao;

import server.persistence.dto.DormitoryDTO;

import java.sql.SQLException;
import java.util.List;

public interface DormitoryDAOI {
    DormitoryDTO findById(Integer id) throws SQLException;
    List<DormitoryDTO> findAll() throws SQLException;
    List<String> findAllIntoString() throws SQLException;
    void save(DormitoryDTO dormitoryDTO) throws SQLException;
    void update(DormitoryDTO dormitoryDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
