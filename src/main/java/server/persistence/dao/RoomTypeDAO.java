package server.persistence.dao;

import server.persistence.dto.RoomTypeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO implements RoomTypeDAOI {

    @Override
    public RoomTypeDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, type_name, description, max_person FROM room_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToRoomTypeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<RoomTypeDTO> findAll() throws SQLException {
        List<RoomTypeDTO> roomTypes = new ArrayList<>();
        String query = "SELECT id, type_name, description, max_person FROM room_types";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                roomTypes.add(mapRowToRoomTypeDTO(resultSet));
            }
        }
        return roomTypes; // 모든 방 유형 정보 반환
    }

    @Override
    public void save(RoomTypeDTO roomTypeDTO) throws SQLException {
        String query = "INSERT INTO room_types (type_name, description, max_person) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roomTypeDTO.getTypeName());
            preparedStatement.setString(2, roomTypeDTO.getDescription());
            preparedStatement.setInt(3, roomTypeDTO.getMaxPerson());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(RoomTypeDTO roomTypeDTO) throws SQLException {
        String query = "UPDATE room_types SET type_name = ?, description = ?, max_person = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roomTypeDTO.getTypeName());
            preparedStatement.setString(2, roomTypeDTO.getDescription());
            preparedStatement.setInt(3, roomTypeDTO.getMaxPerson());
            preparedStatement.setInt(4, roomTypeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM room_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private RoomTypeDTO mapRowToRoomTypeDTO(ResultSet resultSet) throws SQLException {
        return RoomTypeDTO.builder()
                .id(resultSet.getInt("id"))
                .typeName(resultSet.getString("type_name"))
                .description(resultSet.getString("description"))
                .maxPerson(resultSet.getInt("max_person"))
                .build();
    }
}
