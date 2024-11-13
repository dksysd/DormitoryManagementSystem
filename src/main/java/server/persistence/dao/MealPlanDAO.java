package server.persistence.dao;

import server.persistence.dto.MealPlanDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanDAO implements MealPlanDAOI {

    @Override
    public MealPlanDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, price, meal_plan_type_id, dormitory_id FROM meal_plans WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMealPlanDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<MealPlanDTO> findAll() throws SQLException {
        List<MealPlanDTO> mealPlans = new ArrayList<>();
        String query = "SELECT id, price, meal_plan_type_id, dormitory_id FROM meal_plans";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                mealPlans.add(mapRowToMealPlanDTO(resultSet));
            }
        }
        return mealPlans; // 모든 식사 계획 정보 반환
    }

    @Override
    public void save(MealPlanDTO mealPlanDTO) throws SQLException {
        String query = "INSERT INTO meal_plans (price, meal_plan_type_id, dormitory_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealPlanDTO.getPrice());
            preparedStatement.setInt(2, mealPlanDTO.getMealPlanTypeDTO() != null ? mealPlanDTO.getMealPlanTypeDTO().getId() : null);
            preparedStatement.setInt(3, mealPlanDTO.getDormitoryDTO() != null ? mealPlanDTO.getDormitoryDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(MealPlanDTO mealPlanDTO) throws SQLException {
        String query = "UPDATE meal_plans SET price = ?, meal_plan_type_id = ?, dormitory_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mealPlanDTO.getPrice());
            preparedStatement.setInt(2, mealPlanDTO.getMealPlanTypeDTO() != null ? mealPlanDTO.getMealPlanTypeDTO().getId() : null);
            preparedStatement.setInt(3, mealPlanDTO.getDormitoryDTO() != null ? mealPlanDTO.getDormitoryDTO().getId() : null);
            preparedStatement.setInt(4, mealPlanDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM meal_plans WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private MealPlanDTO mapRowToMealPlanDTO(ResultSet resultSet) throws SQLException {
        return MealPlanDTO.builder()
                .id(resultSet.getInt("id"))
                .price(resultSet.getInt("price"))
                .build();
    }
}
