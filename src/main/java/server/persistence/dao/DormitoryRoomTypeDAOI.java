package server.persistence.dao;

import server.persistence.dto.DormitoryRoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface DormitoryRoomTypeDAOI {
    DormitoryRoomTypeDTO findById(Integer id) throws SQLException;
    DormitoryRoomTypeDTO findByUid(String uid) throws SQLException;
    List<DormitoryRoomTypeDTO> findAll() throws SQLException;
    void save(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException;
    void update(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException;
    void updateDormitory(String uid, String dormitoryName) throws SQLException;
    void delete(Integer id) throws SQLException;
}
