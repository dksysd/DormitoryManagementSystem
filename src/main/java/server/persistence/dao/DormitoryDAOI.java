package server.persistence.dao;

import server.persistence.dto.DormitoryDTO;

import java.sql.SQLException;

public interface DormitoryDAOI {
    DormitoryDTO findById(Integer id) throws SQLException;
}
