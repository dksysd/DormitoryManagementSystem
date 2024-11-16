package server.persistence.dao;

import server.persistence.dto.SelectionDTO;
import server.persistence.dto.SelectionApplicationStatusDTO;
import server.persistence.dto.ImageDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionDAO implements SelectionDAOI {

    @Override
    public SelectionDTO findById(Integer id) throws SQLException {
        String query = "SELECT s.id AS selection_id, s.is_final_approved, s.created_at, s.updated_at, " +
                "s.selection_application_status_id, s.tuberculosis_certificate_file_id, s.additional_proof_file_id, " +
                "sas.id AS status_id, sas.status_name " +
                "FROM selections s " +
                "LEFT JOIN selection_application_status sas ON s.selection_application_status_id = sas.id " +
                "WHERE s.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public List<SelectionDTO> findAll() throws SQLException {
        List<SelectionDTO> selections = new ArrayList<>();
        String query = "SELECT s.id AS selection_id, s.is_final_approved, s.created_at, s.updated_at, " +
                "s.selection_application_status_id, s.tuberculosis_certificate_file_id, s.additional_proof_file_id, " +
                "sas.id AS status_id, sas.status_name " +
                "FROM selections s " +
                "LEFT JOIN selection_application_status sas ON s.selection_application_status_id = sas.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                selections.add(mapRowToSelectionDTO(resultSet));
            }
        }
        return selections; // 모든 선택 정보 반환
    }

    @Override
    public void save(SelectionDTO selectionDTO) throws SQLException {
        String query = "INSERT INTO selections (is_final_approved, created_at, updated_at, " +
                "selection_application_status_id, tuberculosis_certificate_file_id, additional_proof_file_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, selectionDTO.isFinalApproved());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionDTO.getUpdatedAt()));
            preparedStatement.setInt(4, selectionDTO.getSelectionApplicationStatusDTO() != null ? selectionDTO.getSelectionApplicationStatusDTO().getId() : null);
            preparedStatement.setInt(5, selectionDTO.getTuberculosisCertificateFileDTO() != null ? selectionDTO.getTuberculosisCertificateFileDTO().getId() : null);
            preparedStatement.setInt(6, selectionDTO.getAdditionalProofFileDTO() != null ? selectionDTO.getAdditionalProofFileDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionDTO selectionDTO) throws SQLException {
        String query = "UPDATE selections SET is_final_approved = ?, created_at = ?, updated_at = ?, " +
                "selection_application_status_id = ?, tuberculosis_certificate_file_id = ?, additional_proof_file_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, selectionDTO.isFinalApproved());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionDTO.getUpdatedAt()));
            preparedStatement.setInt(4, selectionDTO.getSelectionApplicationStatusDTO() != null ? selectionDTO.getSelectionApplicationStatusDTO().getId() : null);
            preparedStatement.setInt(5, selectionDTO.getTuberculosisCertificateFileDTO() != null ? selectionDTO.getTuberculosisCertificateFileDTO().getId() : null);
            preparedStatement.setInt(6, selectionDTO.getAdditionalProofFileDTO() != null ? selectionDTO.getAdditionalProofFileDTO().getId() : null);
            preparedStatement.setInt(7, selectionDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM selections WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SelectionDTO mapRowToSelectionDTO(ResultSet resultSet) throws SQLException {
        SelectionApplicationStatusDTO statusDTO = SelectionApplicationStatusDTO.builder()
                .id(resultSet.getInt("selection_application_status_id"))
                .statusName(resultSet.getString("status_name")) // 상태 이름 추가
                .build();

        ImageDTO tuberculosisCertificateFileDTO = ImageDTO.builder()
                .id(resultSet.getInt("tuberculosis_certificate_file_id")) // ID만 세팅
                .build();

        ImageDTO additionalProofFileDTO = ImageDTO.builder()
                .id(resultSet.getInt("additional_proof_file_id")) // ID만 세팅
                .build();

        return SelectionDTO.builder()
                .id(resultSet.getInt("selection_id"))
                .isFinalApproved(resultSet.getBoolean("is_final_approved"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .selectionApplicationStatusDTO(statusDTO) // 선택 신청 상태 정보 추가
                .tuberculosisCertificateFileDTO(tuberculosisCertificateFileDTO) // 결핵 증명서 파일 정보 추가
                .additionalProofFileDTO(additionalProofFileDTO) // 추가 증명서 파일 정보 추가
                .build();
    }
}
