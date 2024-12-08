package server.persistence.dao;

import server.persistence.dto.PaymentStatusDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//todo userid 넣으면 딱 paymentStatus만 뱉는 거 만들어주세요
public class PaymentStatusDAO implements PaymentStatusDAOI {

    @Override
    public PaymentStatusDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, status_name, description FROM payment_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentStatusDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<PaymentStatusDTO> findAll() throws SQLException {
        List<PaymentStatusDTO> paymentStatuses = new ArrayList<>();
        String query = "SELECT id, status_name, description FROM payment_statuses";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                paymentStatuses.add(mapRowToPaymentStatusDTO(resultSet));
            }
        }
        return paymentStatuses; // 모든 결제 상태 정보 반환
    }

    @Override
    public void save(PaymentStatusDTO paymentStatusDTO) throws SQLException {
        String query = "INSERT INTO payment_statuses (status_name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentStatusDTO.getStatusName());
            preparedStatement.setString(2, paymentStatusDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentStatusDTO paymentStatusDTO) throws SQLException {
        String query = "UPDATE payment_statuses SET status_name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, paymentStatusDTO.getStatusName());
            preparedStatement.setString(2, paymentStatusDTO.getDescription());
            preparedStatement.setInt(3, paymentStatusDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentStatusDTO mapRowToPaymentStatusDTO(ResultSet resultSet) throws SQLException {
        return PaymentStatusDTO.builder()
                .id(resultSet.getInt("id"))
                .statusName(resultSet.getString("status_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
