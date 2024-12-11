package server.persistence.dao;

import server.persistence.dto.MealPlanTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface MealPlanTypeDAOI {
    MealPlanTypeDTO findById(Integer id) throws SQLException;
    MealPlanTypeDTO findByName(String name) throws SQLException;
    List<MealPlanTypeDTO> findAll() throws SQLException;
    void save(MealPlanTypeDTO mealPlanTypeDTO) throws SQLException;
    void update(MealPlanTypeDTO mealPlanTypeDTO) throws SQLException;
    void delete(Integer id) throws SQLException;
}
