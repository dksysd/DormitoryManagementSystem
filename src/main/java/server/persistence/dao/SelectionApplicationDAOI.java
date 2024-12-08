package server.persistence.dao;

import server.persistence.dto.SelectionApplicationDTO;

import java.sql.SQLException;
import java.util.List;

public interface SelectionApplicationDAOI {
    SelectionApplicationDTO findById(Integer id) throws SQLException;
    SelectionApplicationDTO findByUid(String uid) throws SQLException;
    List<SelectionApplicationDTO> findAll() throws SQLException;
    void save(SelectionApplicationDTO selectionApplicationDTO) throws SQLException;
    void update(SelectionApplicationDTO selectionApplicationDTO) throws SQLException;
    void updateMealPlan(String uid, String mealPlanName) throws SQLException;
    void updatePreference(String uid, Integer preference) throws SQLException;
    void updateRoomType(String uid, String roomTypeName) throws SQLException;
    void delete(Integer id) throws SQLException;
}
