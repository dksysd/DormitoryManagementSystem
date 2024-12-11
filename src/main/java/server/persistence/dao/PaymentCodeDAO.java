package server.persistence.dao;

import server.persistence.dto.PaymentCodeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentCodeDAO implements PaymentCodeDAOI {

    @Override
    public PaymentCodeDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, payment_code, description FROM payment_codes WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentCodeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public PaymentCodeDTO findByCode(String code) throws SQLException {
        String query = "SELECT id, payment_code, description FROM payment_codes WHERE payment_code = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentCodeDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<PaymentCodeDTO> findAll() throws SQLException {
        List<PaymentCodeDTO> paymentCodes = new ArrayList<>();
        String query = "SELECT id, payment_code, description FROM payment_codes";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                paymentCodes.add(mapRowToPaymentCodeDTO(resultSet));
            }
        }
        return paymentCodes; // 모든 결제 코드 정보 반환
    }

    @Override
    public void save(PaymentCodeDTO paymentCodeDTO) throws SQLException {
        String query = "INSERT INTO payment_codes (payment_code, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentCodeDTO.getPaymentCode());
            preparedStatement.setString(2, paymentCodeDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentCodeDTO paymentCodeDTO) throws SQLException {
        String query = "UPDATE payment_codes SET payment_code = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentCodeDTO.getPaymentCode());
            preparedStatement.setString(2, paymentCodeDTO.getDescription());
            preparedStatement.setInt(3, paymentCodeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_codes WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentCodeDTO mapRowToPaymentCodeDTO(ResultSet resultSet) throws SQLException {
        return PaymentCodeDTO.builder()
                .id(resultSet.getInt("id"))
                .paymentCode(resultSet.getString("payment_code"))
                .description(resultSet.getString("description"))
                .build();
    }
}
