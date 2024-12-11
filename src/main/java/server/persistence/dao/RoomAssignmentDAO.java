package server.persistence.dao;

import server.persistence.dto.RoomAssignmentDTO;
import server.persistence.dto.SelectionDTO;
import server.persistence.dto.RoomDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomAssignmentDAO implements RoomAssignmentDAOI {

    @Override
    public RoomAssignmentDTO findById(Integer id) throws SQLException {
        String query = "SELECT ra.id AS assignment_id, ra.bed_number, ra.move_in_at, ra.move_out_at, ra.created_at, " +
                "ra.selection_id, ra.room_id, s.id AS selection_id, s.is_final_approved, " +
                "r.id AS room_id, r.room_number " +
                "FROM room_assignments ra " +
                "LEFT JOIN selections s ON ra.selection_id = s.id " +
                "LEFT JOIN rooms r ON ra.room_id = r.id " +
                "WHERE ra.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToRoomAssignmentDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public RoomAssignmentDTO findByUid(String uid) throws SQLException {
        String query = "SELECT ra.id AS assignment_id, ra.bed_number, ra.move_in_at, ra.move_out_at, ra.created_at, " +
                "ra.selection_id, ra.room_id, s.id AS selection_id, s.is_final_approved, " +
                "r.id AS room_id, r.room_number " +
                "FROM room_assignments ra " +
                "LEFT JOIN selections s ON ra.selection_id = s.id " +
                "LEFT JOIN rooms r ON ra.room_id = r.id " +
                "INNER JOIN demerit_points dp ON dp.room_assignment_id = ra.id " +
                "INNER JOIN users u ON u.id = dp.user_id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToRoomAssignmentDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<RoomAssignmentDTO> findAll() throws SQLException {
        List<RoomAssignmentDTO> roomAssignments = new ArrayList<>();
        String query = "SELECT ra.id AS assignment_id, ra.bed_number, ra.move_in_at, ra.move_out_at, ra.created_at, " +
                "ra.selection_id, ra.room_id, s.id AS selection_id, s.is_final_approved, " +
                "r.id AS room_id, r.room_number " +
                "FROM room_assignments ra " +
                "LEFT JOIN selections s ON ra.selection_id = s.id " +
                "LEFT JOIN rooms r ON ra.room_id = r.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                roomAssignments.add(mapRowToRoomAssignmentDTO(resultSet));
            }
        }
        return roomAssignments; // 모든 방 배정 정보 반환
    }

    @Override
    public void save(RoomAssignmentDTO roomAssignmentDTO) throws SQLException {
        String query = "INSERT INTO room_assignments (bed_number, move_in_at, move_out_at, created_at, selection_id, room_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, roomAssignmentDTO.getBedNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(roomAssignmentDTO.getMoveInAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(roomAssignmentDTO.getMoveOutAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(roomAssignmentDTO.getCreatedAt()));
            preparedStatement.setInt(5, roomAssignmentDTO.getSelectionDTO() != null ? roomAssignmentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(6, roomAssignmentDTO.getRoomDTO() != null ? roomAssignmentDTO.getRoomDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(RoomAssignmentDTO roomAssignmentDTO) throws SQLException {
        String query = "UPDATE room_assignments SET bed_number = ?, move_in_at = ?, move_out_at = ?, created_at = ?, selection_id = ?, room_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, roomAssignmentDTO.getBedNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(roomAssignmentDTO.getMoveInAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(roomAssignmentDTO.getMoveOutAt()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(roomAssignmentDTO.getCreatedAt()));
            preparedStatement.setInt(5, roomAssignmentDTO.getSelectionDTO() != null ? roomAssignmentDTO.getSelectionDTO().getId() : null);
            preparedStatement.setInt(6, roomAssignmentDTO.getRoomDTO() != null ? roomAssignmentDTO.getRoomDTO().getId() : null);
            preparedStatement.setInt(7, roomAssignmentDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM room_assignments WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private RoomAssignmentDTO mapRowToRoomAssignmentDTO(ResultSet resultSet) throws SQLException {
        SelectionDTO selectionDTO = SelectionDTO.builder()
                .id(resultSet.getInt("selection_id"))
                .isFinalApproved(resultSet.getBoolean("is_final_approved"))
                .build();

        RoomDTO roomDTO = RoomDTO.builder()
                .id(resultSet.getInt("room_id"))
                .roomNumber(resultSet.getString("room_number"))
                .build();

        return RoomAssignmentDTO.builder()
                .id(resultSet.getInt("assignment_id"))
                .bedNumber(resultSet.getInt("bed_number"))
                .moveInAt(resultSet.getTimestamp("move_in_at").toLocalDateTime())
                .moveOutAt(resultSet.getTimestamp("move_out_at").toLocalDateTime())
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .selectionDTO(selectionDTO) // SelectionDTO 정보 추가
                .roomDTO(roomDTO) // RoomDTO 정보 추가
                .build();
    }
}
