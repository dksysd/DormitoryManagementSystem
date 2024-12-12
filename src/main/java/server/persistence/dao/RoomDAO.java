package server.persistence.dao;

import server.persistence.dto.DormitoryDTO;
import server.persistence.dto.DormitoryRoomTypeDTO;
import server.persistence.dto.RoomDTO;
import server.persistence.dto.RoomTypeDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO implements RoomDAOI {

    @Override
    public RoomDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, room_number, dormitory_room_type_id, dormitory_id FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToRoomDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<RoomDTO> findAll() throws SQLException {
        List<RoomDTO> roomTypes = new ArrayList<>();
        String query = "SELECT id, room_number, dormitory_room_type_id, dormitory_id FROM rooms";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                roomTypes.add(mapRowToRoomDTO(resultSet));
            }
        }
        return roomTypes; // 모든 방 유형 정보 반환
    }

    @Override
    public void save(RoomDTO roomDTO) throws SQLException {
        String query = "INSERT INTO rooms (room_number, dormitory_room_type_id, dormitory_id) VALUES (?, ? ,?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roomDTO.getRoomNumber());
            preparedStatement.setInt(2, roomDTO.getDormitoryRoomTypeDTO().getId());
            preparedStatement.setInt(3, roomDTO.getDormitoryDTO().getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(RoomDTO roomDTO) throws SQLException {
        String query = "UPDATE rooms SET room_number = ?, dormitory_room_type_id = ?, dormitory_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, roomDTO.getRoomNumber());
            preparedStatement.setString(2, roomDTO.getRoomNumber());
            preparedStatement.setInt(3, roomDTO.getDormitoryRoomTypeDTO().getId());
            preparedStatement.setInt(4, roomDTO.getDormitoryDTO().getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private RoomDTO mapRowToRoomDTO(ResultSet resultSet) throws SQLException {
        DormitoryRoomTypeDAO dao = new DormitoryRoomTypeDAO();
        DormitoryRoomTypeDTO dormitoryRoomTypeDTO = dao.findById(resultSet.getInt("dormitory_room_type_id"));

        DormitoryDAO dao2 = new DormitoryDAO();
        DormitoryDTO dormitoryDTO = dao2.findById(resultSet.getInt("dormitory_id"));

        return RoomDTO.builder()
                .id(resultSet.getInt("id"))
                .roomNumber(resultSet.getString("room_number"))
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
