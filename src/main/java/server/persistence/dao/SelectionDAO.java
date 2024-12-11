package server.persistence.dao;

import server.persistence.dto.*;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionDAO implements SelectionDAOI {

    @Override
    public SelectionDTO findById(Integer id) throws SQLException {
        String query = "SELECT s.id AS id, is_final_approved, created_at, updated_at, " +
                "selection_application_id, tuberculosis_certificate_file_id, additional_proof_file_id " +
                "FROM selections s " +
                "LEFT JOIN selection_applications sa ON s.selection_application_id = sa.id " +
                "LEFT JOIN users u ON u.id = sa.user_id " +
                "WHERE u.id = ?";
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
    public SelectionDTO findByUid(String uid) throws SQLException {
        String query = "SELECT s.id AS id, is_final_approved, created_at, updated_at, " +
                "selection_application_id, tuberculosis_certificate_file_id, additional_proof_file_id " +
                "FROM selections s " +
                "LEFT JOIN selection_applications sa ON s.selection_application_id = sa.id " +
                "LEFT JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSelectionDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public void updateTuber(String uid, Byte[] data) throws SQLException {
        String query = "SELECT s.id AS id, u.user_name AS name, u.id AS user_id " +
                "FROM selections s " +
                "LEFT JOIN selection_applications sa ON s.selection_application_id = sa.id " +
                "LEFT JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        int id;
        int user_id;
        String name;
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            user_id = resultSet.getInt("user_id");
        }

        UserDAO dao = new UserDAO();
        ImageDAO imageDAO = new ImageDAO();
        UserDTO userDTO = dao.findById(user_id);
        ImageDTO imageDTO = ImageDTO.builder()
                .name(name + "_tuber")
                .data(data)
                .width(16)
                .height(16)
                .extension("jpg")
                .userDTO(userDTO)
                .build();
        int imageId = imageDAO.save(imageDTO);

        query = "UPDATE selections s SET tuberculosis_certificate_file_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateProof(String uid, Byte[] data) throws SQLException {
        String query = "SELECT s.id AS id, u.user_name AS name, u.id AS user_id " +
                "FROM selections s " +
                "LEFT JOIN selection_applications sa ON s.selection_application_id = sa.id " +
                "LEFT JOIN users u ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        int id;
        int user_id;
        String name;
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getInt("id");
            name = resultSet.getString("name");
            user_id = resultSet.getInt("user_id");
        }

        UserDAO dao = new UserDAO();
        ImageDAO imageDAO = new ImageDAO();
        UserDTO userDTO = dao.findById(user_id);
        ImageDTO imageDTO = ImageDTO.builder()
                .name(name + "_proof")
                .data(data)
                .width(16)
                .height(16)
                .extension("jpg")
                .userDTO(userDTO)
                .build();
        int imageId = imageDAO.save(imageDTO);

        query = "UPDATE selections s SET additional_proof_file_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, imageId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public List<SelectionDTO> findAll() throws SQLException {
        List<SelectionDTO> selections = new ArrayList<>();
        String query = "SELECT s.id AS selection_id, s.is_final_approved, s.created_at, s.updated_at, " +
                "s.selection_application_id, s.tuberculosis_certificate_file_id, s.additional_proof_file_id, " +
                "sa.id AS application_id " +
                "FROM selections s " +
                "INNER JOIN selection_applications sa ON s.selection_application_id = sa.id";
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
                "selection_application_id, tuberculosis_certificate_file_id, additional_proof_file_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, selectionDTO.isFinalApproved());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionDTO.getUpdatedAt()));
            preparedStatement.setInt(4, selectionDTO.getSelectionApplicationDTO() != null ? selectionDTO.getSelectionApplicationDTO().getId() : null);
            preparedStatement.setInt(5, selectionDTO.getTuberculosisCertificateFileDTO() != null ? selectionDTO.getTuberculosisCertificateFileDTO().getId() : null);
            preparedStatement.setInt(6, selectionDTO.getAdditionalProofFileDTO() != null ? selectionDTO.getAdditionalProofFileDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SelectionDTO selectionDTO) throws SQLException {
        String query = "UPDATE selections SET is_final_approved = ?, created_at = ?, updated_at = ?, " +
                "selection_application_id = ?, tuberculosis_certificate_file_id = ?, additional_proof_file_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, selectionDTO.isFinalApproved());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(selectionDTO.getCreatedAt()));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(selectionDTO.getUpdatedAt()));
            preparedStatement.setInt(4, selectionDTO.getSelectionApplicationDTO() != null ? selectionDTO.getSelectionApplicationDTO().getId() : null);
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
        SelectionApplicationDTO selectionApplicationDTO = SelectionApplicationDTO.builder()
                .id(resultSet.getInt("application_id"))
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
                .selectionApplicationDTO(selectionApplicationDTO) // 선택 신청 상태 정보 추가
                .tuberculosisCertificateFileDTO(tuberculosisCertificateFileDTO) // 결핵 증명서 파일 정보 추가
                .additionalProofFileDTO(additionalProofFileDTO) // 추가 증명서 파일 정보 추가
                .build();
    }
}
