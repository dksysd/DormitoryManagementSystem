package server.persistence.dao;

import server.persistence.dto.MealPlanDTO;

import java.sql.SQLException;

public interface MealPlanDAOI {
    MealPlanDTO findById(Integer id) throws SQLException;
}
