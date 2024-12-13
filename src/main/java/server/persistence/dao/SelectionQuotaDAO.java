package server.persistence.dao;

import server.persistence.dto.DormitoryRoomTypeDTO;
import server.persistence.dto.SelectionQuotaDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.SelectionScheduleDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionQuotaDAO implements SelectionQuotaDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public SelectionQuotaDTO findById(Integer id) throws SQLException {
        String query = "SELECT s.id, s.quota, s.selection_schedule_id, s.dormitory_room_type_id FROM selection_quotas s WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionQuotaDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<SelectionQuotaDTO> findAll() throws SQLException {
        List<SelectionQuotaDTO> selectionQuotas = new ArrayList<>();
        String query = "SELECT id, quota, selection_schedule_id, dormitory_room_type_id FROM selection_quotas";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selectionQuotas.add(mapRowToSelectionQuotaDTO(resultSet));
            }
        }
        return selectionQuotas; // 모든 선택 할당량 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(SelectionQuotaDTO selectionQuotaDTO) throws SQLException {
        String query = "INSERT INTO selection_quotas (quota, selection_schedule_id, dormitory_room_type_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionQuotaDTO.getQuota());
            preparedStatement.setInt(2, selectionQuotaDTO.getSelectionScheduleDTO() != null ? selectionQuotaDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.setInt(3, selectionQuotaDTO.getDormitoryRoomTypeDTO() != null ? selectionQuotaDTO.getDormitoryRoomTypeDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(SelectionQuotaDTO selectionQuotaDTO) throws SQLException {
        String query = "UPDATE selection_quotas SET quota = ?, selection_schedule_id = ?, dormitory_room_type_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, selectionQuotaDTO.getQuota());
            preparedStatement.setInt(2, selectionQuotaDTO.getSelectionScheduleDTO() != null ? selectionQuotaDTO.getSelectionScheduleDTO().getId() : null);
            preparedStatement.setInt(3, selectionQuotaDTO.getDormitoryRoomTypeDTO() != null ? selectionQuotaDTO.getDormitoryRoomTypeDTO().getId() : null);
            preparedStatement.setInt(4, selectionQuotaDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selection_quotas WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private SelectionQuotaDTO mapRowToSelectionQuotaDTO(ResultSet resultSet) throws SQLException {
        SelectionScheduleDAO dao = new SelectionScheduleDAO();
        SelectionScheduleDTO scheduleDTO = dao.findById(resultSet.getInt("selection_schedule_id"));

        DormitoryRoomTypeDAO dao2 = new DormitoryRoomTypeDAO();
        DormitoryRoomTypeDTO dormitoryRoomTypeDTO = dao2.findById(resultSet.getInt("dormitory_room_type_id"));

        return SelectionQuotaDTO.builder()
                .id(resultSet.getInt("id"))
                .quota(resultSet.getInt("quota"))
                .selectionScheduleDTO(scheduleDTO) // ID만 세팅
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO) // ID만 세팅
                .build();
    }
}
