package server.persistence.dao;

import server.persistence.dto.DormitoryDTO;
import server.persistence.dto.DormitoryRoomTypeDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.RoomTypeDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DormitoryRoomTypeDAO implements DormitoryRoomTypeDAOI {

    @Override
    public DormitoryRoomTypeDTO findById(Integer id) throws SQLException {
        String query = "SELECT drt.id AS dormitory_room_type_id, drt.price, drt.created_at, drt.updated_at, drt.room_type_id, drt.dormitory_id" +
                ", rt.type_name AS typeName, rt.max_person AS maxPerson, " +
                "d.name AS dormitoryName " +
                "FROM dormitory_room_types drt " +
                "INNER JOIN dormitories d ON d.id = drt.dormitory_id " +
                "INNER JOIN room_types rt ON rt.id = drt.room_type_id " +
                "INNER JOIN selection_applications sa ON sa.dormitory_room_type_id = drt.id " +
                "INNER JOIN users u ON u.id = sa.user_id " +
                "WHERE drt.id = ?";
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
    public DormitoryRoomTypeDTO findByUid(String uid) throws SQLException {
        String query = "SELECT drt.id AS dormitory_room_type_id, drt.price, drt.created_at, drt.updated_at, drt.room_type_id, drt.dormitory_id" +
                ", rt.type_name AS typeName, rt.max_person AS maxPerson, " +
                "d.name AS dormitoryName " +
                "FROM dormitory_room_types drt " +
                "INNER JOIN dormitories d ON d.id = drt.dormitory_id " +
                "INNER JOIN room_types rt ON rt.id = drt.room_type_id " +
                "INNER JOIN selection_applications sa ON sa.dormitory_room_type_id = drt.id " +
                "INNER JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?" ;
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapRowToDormitoryRoomTypeDTO(resultSet);
        }
    }

    @Override
    public List<DormitoryRoomTypeDTO> findAll() throws SQLException {
        List<DormitoryRoomTypeDTO> roomTypes = new ArrayList<>();
        String query = "SELECT drt.id AS dormitory_room_type_id, drt.price, drt.created_at, drt.updated_at, drt.room_type_id, drt.dormitory_id" +
                ", rt.type_name AS typeName, rt.max_person AS maxPerson, " +
                "d.name AS dormitoryName " +
                "FROM dormitory_room_types drt " +
                "INNER JOIN dormitories d ON d.id = drt.dormitory_id " +
                "INNER JOIN room_types rt ON rt.id = drt.room_type_id " +
                "INNER JOIN selection_applications sa ON sa.dormitory_room_type_id = drt.id " +
                "INNER JOIN users u ON u.id = sa.user_id " +
                "WHERE id = ?";
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
    public void updateDormitory(String uid, String dormitoryName) throws SQLException {
        String query = "SELECT drt.id AS dormitory_room_type_id, drt.price, drt.created_at, drt.updated_at, drt.room_type_id, drt.dormitory_id" +
                ", rt.type_name AS typeName, rt.max_person AS maxPerson, " +
                "d.name AS dormitoryName " +
                "FROM dormitory_room_types drt " +
                "INNER JOIN dormitories d ON d.id = drt.dormitory_id " +
                "INNER JOIN room_types rt ON rt.id = drt.room_type_id " +
                "INNER JOIN selection_applications sa ON sa.dormitory_room_type_id = drt.id " +
                "INNER JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        int dormitory_id;
        int id;

        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dormitoryName);
            preparedStatement.setString(2, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt(1);
            dormitory_id = resultSet.getInt(2);
        }

        query = "UPDATE dormitory_room_types SET dormitory_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dormitory_id);
            preparedStatement.setInt(2, id);
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
        RoomTypeDAO dao1 = new RoomTypeDAO();
        RoomTypeDTO roomTypeDTO = dao1.findById(resultSet.getInt("room_type_id"));

        DormitoryDAO dao2 = new DormitoryDAO();
        DormitoryDTO dormitoryDTO = dao2.findById(resultSet.getInt("dormitory_id"));

        return DormitoryRoomTypeDTO.builder()
                .id(resultSet.getInt("dormitory_room_type_id"))
                .price(resultSet.getInt("price"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .roomTypeDTO(roomTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
