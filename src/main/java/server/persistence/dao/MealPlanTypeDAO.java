package server.persistence.dao;

import server.persistence.dto.MealPlanTypeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanTypeDAO implements MealPlanTypeDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public MealPlanTypeDTO findByName(String name) throws SQLException {
        String query = "SELECT id, type_name, description FROM meal_plan_types WHERE type_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMealPlanTypeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM meal_plan_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private MealPlanTypeDTO mapRowToMealPlanTypeDTO(ResultSet resultSet) throws SQLException {
        return MealPlanTypeDTO.builder()
                .id(resultSet.getInt("id"))
                .typeName(resultSet.getString("type_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
