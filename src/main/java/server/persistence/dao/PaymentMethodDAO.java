package server.persistence.dao;

import server.persistence.dto.PaymentMethodDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAO implements PaymentMethodDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // 이름을 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_methods WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private PaymentMethodDTO mapRowToPaymentMethodDTO(ResultSet resultSet) throws SQLException {
        return PaymentMethodDTO.builder()
                .id(resultSet.getInt("id"))
                .methodName(resultSet.getString("method_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
