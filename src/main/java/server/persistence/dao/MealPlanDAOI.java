package server.persistence.dao;

import server.persistence.dto.MealPlanDTO;

import java.sql.SQLException;
import java.util.List;

public interface MealPlanDAOI {
    MealPlanDTO findById(Integer id) throws SQLException;
    List<MealPlanDTO> findAll() throws SQLException;
    void save(MealPlanDTO mealPlanDTO) throws SQLException;
    void update(MealPlanDTO mealPlanDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
