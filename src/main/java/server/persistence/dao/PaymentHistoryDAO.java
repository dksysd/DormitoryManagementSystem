package server.persistence.dao;

import server.persistence.dto.PaymentDTO;
import server.persistence.dto.PaymentHistoryDTO;
import server.persistence.dto.UserDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryDAO implements PaymentHistoryDAOI {

    @Override
    public PaymentHistoryDTO findById(Integer id) throws SQLException {
        String query = "SELECT ph.id AS history_id, ph.user_id, ph.payment_id, " +
                "u.id AS user_id, u.user_name, u.phone_number, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM payment_histories ph " +
                "LEFT JOIN users u ON ph.user_id = u.id " +
                "LEFT JOIN payments p ON ph.payment_id = p.id " +
                "WHERE ph.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPaymentHistoryDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<PaymentHistoryDTO> findAll() throws SQLException {
        List<PaymentHistoryDTO> paymentHistories = new ArrayList<>();
        String query = "SELECT ph.id AS history_id, ph.user_id, ph.payment_id, " +
                "u.id AS user_id, u.user_name, u.phone_number, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM payment_histories ph " +
                "LEFT JOIN users u ON ph.user_id = u.id " +
                "LEFT JOIN payments p ON ph.payment_id = p.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                paymentHistories.add(mapRowToPaymentHistoryDTO(resultSet));
            }
        }
        return paymentHistories; // 모든 결제 기록 정보 반환
    }

    @Override
    public void save(PaymentHistoryDTO paymentHistoryDTO) throws SQLException {
        String query = "INSERT INTO payment_histories (user_id, payment_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, paymentHistoryDTO.getUserDTO() != null ? paymentHistoryDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(2, paymentHistoryDTO.getPaymentDTO() != null ? paymentHistoryDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(PaymentHistoryDTO paymentHistoryDTO) throws SQLException {
        String query = "UPDATE payment_histories SET user_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, paymentHistoryDTO.getUserDTO() != null ? paymentHistoryDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(2, paymentHistoryDTO.getPaymentDTO() != null ? paymentHistoryDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(3, paymentHistoryDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM payment_histories WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private PaymentHistoryDTO mapRowToPaymentHistoryDTO(ResultSet resultSet) throws SQLException {
        UserDAO dao = new UserDAO();
        UserDTO userDTO = dao.findById(resultSet.getInt("user_id"));

        PaymentDAO dao2 = new PaymentDAO();
        PaymentDTO paymentDTO = dao2.findById(resultSet.getInt("payment_id"));

        return PaymentHistoryDTO.builder()
                .id(resultSet.getInt("history_id"))
                .userDTO(userDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
