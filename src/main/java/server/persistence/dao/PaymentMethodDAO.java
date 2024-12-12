package server.persistence.dao;

import server.persistence.dto.PaymentMethodDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAO implements PaymentMethodDAOI {

    @Override
    public PaymentMethodDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, method_name, description FROM payment_methods WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentMethodDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public PaymentMethodDTO findByName(String name) throws SQLException {
        String query = "SELECT id, method_name, description FROM payment_methods WHERE method_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentMethodDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<PaymentMethodDTO> findAll() throws SQLException {
        List<PaymentMethodDTO> genderCodes = new ArrayList<>();
        String query = "SELECT id, method_name, description FROM payment_methods";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                genderCodes.add(mapRowToPaymentMethodDTO(resultSet));
            }
        }
        return genderCodes; // 모든 성별 코드 정보 반환
    }

    @Override
    public void save(PaymentMethodDTO PaymentMethodDTO) throws SQLException {
        String query = "INSERT INTO payment_methods (method_name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, PaymentMethodDTO.getMethodName());
            preparedStatement.setString(2, PaymentMethodDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentMethodDTO PaymentMethodDTO) throws SQLException {
        String query = "UPDATE payment_methods SET method_name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, PaymentMethodDTO.getMethodName());
            preparedStatement.setString(2, PaymentMethodDTO.getDescription());
            preparedStatement.setInt(3, PaymentMethodDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_methods WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentMethodDTO mapRowToPaymentMethodDTO(ResultSet resultSet) throws SQLException {
        return PaymentMethodDTO.builder()
                .id(resultSet.getInt("id"))
                .methodName(resultSet.getString("method_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
