package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;
import server.persistence.dto.CardPaymentDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.PaymentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentDAO implements CardPaymentDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public CardPaymentDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardPaymentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // 카드번호를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public CardPaymentDTO findByCardNumber(String cardNumber) throws SQLException {
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments WHERE card_number = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToCardPaymentDTO(resultSet);
            }
        }
        return null;
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<CardPaymentDTO> findAll() throws SQLException {
        List<CardPaymentDTO> payments = new ArrayList<>();
        String query = "SELECT id, card_number, created_at, card_issuer_id, payment_id FROM card_payments";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                payments.add(mapRowToCardPaymentDTO(resultSet));
            }
        }
        return payments; // 모든 카드 결제 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(CardPaymentDTO cardPaymentDTO) throws SQLException {
        String query = "INSERT INTO card_payments (card_number, created_at, card_issuer_id, payment_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardPaymentDTO.getCardNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(cardPaymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, cardPaymentDTO.getCardIssuerDTO() != null ? cardPaymentDTO.getCardIssuerDTO().getId() : null);
            preparedStatement.setInt(4, cardPaymentDTO.getPaymentDTO() != null ? cardPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(CardPaymentDTO cardPaymentDTO) throws SQLException {
        String query = "UPDATE card_payments SET card_number = ?, created_at = ?, card_issuer_id = ?, payment_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardPaymentDTO.getCardNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(cardPaymentDTO.getCreatedAt()));
            preparedStatement.setInt(3, cardPaymentDTO.getCardIssuerDTO() != null ? cardPaymentDTO.getCardIssuerDTO().getId() : null);
            preparedStatement.setInt(4, cardPaymentDTO.getPaymentDTO() != null ? cardPaymentDTO.getPaymentDTO().getId() : null);
            preparedStatement.setInt(5, cardPaymentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 필요 데이터를 각각 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(String uid, String cardNumber, String cardIssuerName, String paymentStatus) throws SQLException {
        String query = "SELECT cp.id, ci.id, p.id, ps.id " +
                "FROM card_payments cp " +
                "INNER JOIN payments p ON p.id = cp.payment_id " +
                "INNER JOIN card_issuers ci ON ci.issuer_name = ? " +
                "INNER JOIN payment_statuses ps ON ps.status_name = ? " +
                "INNER JOIN payment_histories ph ON p.id = ph.payment_id " +
                "INNER JOIN users u ON u.id = ph.user_id " +
                "WHERE u.uid = ?";
        int cardId;
        int paymentId;
        int issuerId;
        int statusId;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, cardIssuerName);
            preparedStatement.setString(2, paymentStatus);
            preparedStatement.setString(3, uid);

            ResultSet resultSet = preparedStatement.executeQuery();

            cardId = resultSet.getInt(1);
            paymentId = resultSet.getInt(2);
            issuerId = resultSet.getInt(3);
            statusId = resultSet.getInt(4);
        }

        query = "UPDATE card_payments SET card_number = ?, card_issuer_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,cardNumber);
            preparedStatement.setInt(2, issuerId);
            preparedStatement.setInt(3, cardId);

            preparedStatement.executeUpdate();
        }

        query = "UPDATE payments SET payment_status_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, statusId);
            preparedStatement.setInt(2,paymentId);
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM card_payments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private CardPaymentDTO mapRowToCardPaymentDTO(ResultSet resultSet) throws SQLException {
        CardIssuerDAO dao1 = new CardIssuerDAO();
        CardIssuerDTO cardIssuerDTO = dao1.findById(resultSet.getInt("card_issuer_id"));
        PaymentDAO paymentDAO = new PaymentDAO();
        PaymentDTO paymentDTO = paymentDAO.findById(resultSet.getInt("payment_id"));

        return CardPaymentDTO.builder()
                .id(resultSet.getInt("id"))
                .cardNumber(resultSet.getString("card_number"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .cardIssuerDTO(cardIssuerDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
