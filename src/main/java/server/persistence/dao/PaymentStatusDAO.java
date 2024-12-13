package server.persistence.dao;

import server.persistence.dto.PaymentStatusDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentStatusDAO implements PaymentStatusDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private PaymentStatusDTO mapRowToPaymentStatusDTO(ResultSet resultSet) throws SQLException {
        return PaymentStatusDTO.builder()
                .id(resultSet.getInt("id"))
                .statusName(resultSet.getString("status_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
