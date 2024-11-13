package server.persistence.dao;

import server.persistence.dto.DormitoryRoomTypeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormitoryRoomTypeDAO implements DormitoryRoomTypeDAOI {

    @Override
    public DormitoryRoomTypeDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, price, created_at, updated_at, room_type_id, dormitory_id FROM dormitory_room_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToDormitoryRoomTypeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<DormitoryRoomTypeDTO> findAll() throws SQLException {
        List<DormitoryRoomTypeDTO> roomTypes = new ArrayList<>();
        String query = "SELECT id, price, created_at, updated_at, room_type_id, dormitory_id FROM dormitory_room_types";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                roomTypes.add(mapRowToDormitoryRoomTypeDTO(resultSet));
            }
        }
        return roomTypes; // 모든 기숙사 방 유형 정보 반환
    }

    @Override
    public void save(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException {
        String query = "INSERT INTO dormitory_room_types (price, created_at, updated_at, room_type_id, dormitory_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, dormitoryRoomTypeDTO.getPrice());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dormitoryRoomTypeDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dormitoryRoomTypeDTO.getUpdatedAt()));
            preparedStatement.setInt(4, dormitoryRoomTypeDTO.getRoomTypeDTO() != null ? dormitoryRoomTypeDTO.getRoomTypeDTO().getId() : null);
            preparedStatement.setInt(5, dormitoryRoomTypeDTO.getDormitoryDTO() != null ? dormitoryRoomTypeDTO.getDormitoryDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException {
        String query = "UPDATE dormitory_room_types SET price = ?, created_at = ?, updated_at = ?, room_type_id = ?, dormitory_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, dormitoryRoomTypeDTO.getPrice());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dormitoryRoomTypeDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dormitoryRoomTypeDTO.getUpdatedAt()));
            preparedStatement.setInt(4, dormitoryRoomTypeDTO.getRoomTypeDTO() != null ? dormitoryRoomTypeDTO.getRoomTypeDTO().getId() : null);
            preparedStatement.setInt(5, dormitoryRoomTypeDTO.getDormitoryDTO() != null ? dormitoryRoomTypeDTO.getDormitoryDTO().getId() : null);
            preparedStatement.setInt(6, dormitoryRoomTypeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM dormitory_room_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private DormitoryRoomTypeDTO mapRowToDormitoryRoomTypeDTO(ResultSet resultSet) throws SQLException {
        return DormitoryRoomTypeDTO.builder()
                .id(resultSet.getInt("id"))
                .price(resultSet.getInt("price"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}
