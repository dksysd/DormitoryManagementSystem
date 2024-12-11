package server.persistence.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import server.persistence.dto.MoveOutRequestDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveOutRequestDAO implements MoveOutRequestDAOI {

    @Override
    public MoveOutRequestDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, checkout_at,expect_checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, bank_id FROM move_out_requests WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMoveOutRequestDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public MoveOutRequestDTO findByUid(String uid) throws SQLException {
        String query = "SELECT mor.id AS id, mor.checkout_at AS checkout_at, mor.expect_checkout_at AS expect_checkout_at" +
                "mor.account_number AS account_number, mor.created_at AS created_at, " +
                "mor.updated_at AS updated_at , mor.move_out_request_status_id AS move_out_request_status_id" +
                ", mor.selection_id AS selection_id, mor.bank_id AS bank_id" +
                "FROM move_out_requests mor" +
                "INNER JOIN selections s ON s.id = mor.selection_id" +
                "INNER JOIN selection_applications sa ON sa.id = s.selection_application_id" +
                "INNER JOIN user u ON u.id = sa.user_id" +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMoveOutRequestDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<MoveOutRequestDTO> findAll() throws SQLException {
        List<MoveOutRequestDTO> moveOutRequests = new ArrayList<>();
        String query = "SELECT id, checkout_at, expect_checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, bank_id FROM move_out_requests";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                moveOutRequests.add(mapRowToMoveOutRequestDTO(resultSet));
            }
        }
        return moveOutRequests; // 모든 퇴실 요청 정보 반환
    }

    @Override
    public List<String> findAllOfMoveOut() throws SQLException {
        List<String> moveOutRequests = new ArrayList<>();
        String query = "SELECT u.uid AS uid, d.name AS dormitory_name " +
                "FROM move_out_request mor " +
                "INNER JOIN selections s ON s.id = mor.selection_application_id" +
                "INNER JOIN selection_applications sa ON s.selection_application_id = sa.id" +
                "INNER JOIN user u ON sa.user_id = u.id" +
                "INNER JOIN dormitory_user_types dut ON dut.id = sa.dormitory_room_type_id" +
                "INNER JOIN dormitory d ON dut.dormitory_id = d.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            moveOutRequests.add(resultSet.getString("dormitory_name")
                    + " : " + resultSet.getString("uid"));
        }
        return moveOutRequests;
    }

    @Override
    public void save(MoveOutRequestDTO moveOutRequestDTO) throws SQLException {
        String query = "INSERT INTO move_out_requests (checkout_at,expect_checkout_at, account_number, created_at, updated_at, move_out_request_status_id, selection_id, payment_refund_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(moveOutRequestDTO.getCheckoutAt()));
            preparedStatement.setString(2, moveOutRequestDTO.getAccountNumber());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(moveOutRequestDTO.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(moveOutRequestDTO.getUpdatedAt()));
            preparedStatement.setInt(5, moveOutRequestDTO.getMoveOutRequestStatusDTO() != null ? moveOutRequestDTO.getMoveOutRequestStatusDTO().getId() : null);
            preparedStatement.setInt(6, moveOutRequestDTO.getSelectionDTO() != null ? moveOutRequestDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(7, moveOutRequestDTO.getPaymentRefundDTO() != null ? moveOutRequestDTO.getPaymentRefundDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(MoveOutRequestDTO moveOutRequestDTO) throws SQLException {
        String query = "UPDATE move_out_requests SET checkout_at = ?, account_number = ?, created_at = ?, updated_at = ?, move_out_request_status_id = ?, selection_id = ?, payment_refund_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(moveOutRequestDTO.getCheckoutAt()));
            preparedStatement.setString(2, moveOutRequestDTO.getAccountNumber());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(moveOutRequestDTO.getCreatedAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(moveOutRequestDTO.getUpdatedAt()));
            preparedStatement.setInt(5, moveOutRequestDTO.getMoveOutRequestStatusDTO() != null ? moveOutRequestDTO.getMoveOutRequestStatusDTO().getId() : null);
            preparedStatement.setInt(6, moveOutRequestDTO.getSelectionDTO() != null ? moveOutRequestDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(7, moveOutRequestDTO.getPaymentRefundDTO() != null ? moveOutRequestDTO.getPaymentRefundDTO().getId() : null);
            preparedStatement.setInt(8, moveOutRequestDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateStatus(String uid, String status) throws SQLException {
        String query = "SELECT mor.id AS request_id FROM move_out_requests mor" +
                "INNER JOIN selections s ON mor.selection_id = s.id" +
                "INNER JOIN selection_applications sa ON sa.id = s.selection_application_id" +
                "INNER JOIN users u ON u.id = sa.user_id" +
                "WHERE u.uid = ?";
        int id;
        int status_id;
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,uid);

            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt("request_id");
        }

        query = "SELECT id FROM move_out_request_statuses" +
                "WHERE status_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,status);
            ResultSet resultSet = preparedStatement.executeQuery();
            status_id = resultSet.getInt("id");
        }

        query = "UPDATE move_out_requests SET move_out_status_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, status_id);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM move_out_requests WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private MoveOutRequestDTO mapRowToMoveOutRequestDTO(ResultSet resultSet) throws SQLException {
        return MoveOutRequestDTO.builder()
                .id(resultSet.getInt("id"))
                .checkoutAt(resultSet.getTimestamp("checkout_at").toLocalDateTime())
                .expectCheckoutAt(resultSet.getTimestamp("expect_checkout_at").toLocalDateTime())
                .accountNumber(resultSet.getString("account_number"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}
