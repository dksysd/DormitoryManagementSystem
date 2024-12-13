package server.persistence.dao;

import server.persistence.dto.MoveOutRequestStatusDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoveOutRequestStatusDAO implements MoveOutRequestStatusDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public MoveOutRequestStatusDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, status_name, description FROM move_out_request_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToMoveOutRequestStatusDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<MoveOutRequestStatusDTO> findAll() throws SQLException {
        List<MoveOutRequestStatusDTO> statuses = new ArrayList<>();
        String query = "SELECT id, status_name, description FROM move_out_request_statuses";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                statuses.add(mapRowToMoveOutRequestStatusDTO(resultSet));
            }
        }
        return statuses; // 모든 퇴실 요청 상태 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(MoveOutRequestStatusDTO moveOutRequestStatusDTO) throws SQLException {
        String query = "INSERT INTO move_out_request_statuses (status_name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, moveOutRequestStatusDTO.getStatusName());
            preparedStatement.setString(2, moveOutRequestStatusDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(MoveOutRequestStatusDTO moveOutRequestStatusDTO) throws SQLException {
        String query = "UPDATE move_out_request_statuses SET status_name = ?, description = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, moveOutRequestStatusDTO.getStatusName());
            preparedStatement.setString(2, moveOutRequestStatusDTO.getDescription());
            preparedStatement.setInt(3, moveOutRequestStatusDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM move_out_request_statuses WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private MoveOutRequestStatusDTO mapRowToMoveOutRequestStatusDTO(ResultSet resultSet) throws SQLException {
        return MoveOutRequestStatusDTO.builder()
                .id(resultSet.getInt("id"))
                .statusName(resultSet.getString("status_name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
