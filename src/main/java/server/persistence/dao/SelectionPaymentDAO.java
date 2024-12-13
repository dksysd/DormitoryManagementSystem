package server.persistence.dao;

import server.persistence.dto.SelectionPaymentDTO;
import server.persistence.dto.SelectionDTO;
import server.persistence.dto.PaymentDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionPaymentDAO implements SelectionPaymentDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public SelectionPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT sp.id AS payment_id, sp.selection_id, sp.payment_id, " +
                "s.id AS selection_id, s.is_final_approved, s.created_at AS selection_created_at, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM selection_payments sp " +
                "LEFT JOIN selections s ON sp.selection_id = s.id " +
                "LEFT JOIN payments p ON sp.payment_id = p.id " +
                "WHERE sp.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<SelectionPaymentDTO> findAll() throws SQLException {
        List<SelectionPaymentDTO> selectionPayments = new ArrayList<>();
        String query = "SELECT sp.id AS payment_id, sp.selection_id, sp.payment_id, " +
                "s.id AS selection_id, s.is_final_approved, s.created_at AS selection_created_at, " +
                "p.id AS payment_id, p.payment_amount, p.created_at AS payment_created_at " +
                "FROM selection_payments sp " +
                "LEFT JOIN selections s ON sp.selection_id = s.id " +
                "LEFT JOIN payments p ON sp.payment_id = p.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionPayments.add(mapRowToSelectionPaymentDTO(resultSet));
            }
        }
        return selectionPayments; // 모든 선택 결제 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(SelectionPaymentDTO selectionPaymentDTO) throws SQLException {
        String query = "INSERT INTO selection_payments (selection_id, payment_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionPaymentDTO.getSelectionDTO() != null ? selectionPaymentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(2, selectionPaymentDTO.getPaymentDTO() != null ? selectionPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(SelectionPaymentDTO selectionPaymentDTO) throws SQLException {
        String query = "UPDATE selection_payments SET selection_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionPaymentDTO.getSelectionDTO() != null ? selectionPaymentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(2, selectionPaymentDTO.getPaymentDTO() != null ? selectionPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(3, selectionPaymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private SelectionPaymentDTO mapRowToSelectionPaymentDTO(ResultSet resultSet) throws SQLException {
        SelectionDAO dao = new SelectionDAO();
        SelectionDTO selectionDTO = dao.findById(resultSet.getInt("selection_id"));

        PaymentDAO paymentDAO = new PaymentDAO();
        PaymentDTO paymentDTO = paymentDAO.findById(resultSet.getInt("payment_id"));

        return SelectionPaymentDTO.builder()
                .id(resultSet.getInt("payment_id"))
                .selectionDTO(selectionDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
