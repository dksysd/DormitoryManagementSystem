package server.persistence.dao;

import server.persistence.dto.MealPlanTypeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanTypeDAO implements MealPlanTypeDAOI {

    @Override
    public MealPlanTypeDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, type_name, description FROM meal_plan_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMealPlanTypeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<MealPlanTypeDTO> findAll() throws SQLException {
        List<MealPlanTypeDTO> mealPlanTypes = new ArrayList<>();
        String query = "SELECT id, type_name, description FROM meal_plan_types";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                mealPlanTypes.add(mapRowToMealPlanTypeDTO(resultSet));
            }
        }
        return mealPlanTypes; // 모든 식사 계획 유형 정보 반환
    }

    @Override
    public void save(MealPlanTypeDTO mealPlanTypeDTO) throws SQLException {
        String query = "INSERT INTO meal_plan_types (type_name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, mealPlanTypeDTO.getTypeName());
            preparedStatement.setString(2, mealPlanTypeDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(MealPlanTypeDTO mealPlanTypeDTO) throws SQLException {
        String query = "UPDATE meal_plan_types SET type_name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, mealPlanTypeDTO.getTypeName());
            preparedStatement.setString(2, mealPlanTypeDTO.getDescription());
            preparedStatement.setInt(3, mealPlanTypeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM meal_plan_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private MealPlanTypeDTO mapRowToMealPlanTypeDTO(ResultSet resultSet) throws SQLException {
        return MealPlanTypeDTO.builder()
                .id(resultSet.getInt("id"))
                .typeName(resultSet.getString("type_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
