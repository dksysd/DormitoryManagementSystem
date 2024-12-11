package server.persistence.dao;

import server.persistence.dto.SubjectDTO;
import server.persistence.dto.UserDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO implements SubjectDAOI {

    @Override
    public SubjectDTO findById(Integer id) throws SQLException {
        String query = "SELECT s.id, s.subject_name, s.description, s.credit, s.created_at, s.updated_at, " +
                "s.professor_user_id, u.user_name AS professor_name " +
                "FROM subjects s " +
                "LEFT JOIN users u ON s.professor_user_id = u.id " +
                "WHERE s.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSubjectDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public SubjectDTO findByName(String subjectName) throws SQLException {
        String query = "SELECT s.id, s.subject_name, s.description, s.credit, s.created_at, s.updated_at, " +
                "s.professor_user_id, u.user_name AS professor_name " +
                "FROM subjects s " +
                "LEFT JOIN users u ON s.professor_user_id = u.id " +
                "WHERE s.subject_name = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, subjectName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToSubjectDTO(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<SubjectDTO> findAll() throws SQLException {
        List<SubjectDTO> subjects = new ArrayList<>();
        String query = "SELECT s.id, s.subject_name, s.description, s.credit, s.created_at, s.updated_at, " +
                "s.professor_user_id, u.user_name AS professor_name " +
                "FROM subjects s " +
                "LEFT JOIN users u ON s.professor_user_id = u.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                subjects.add(mapRowToSubjectDTO(resultSet));
            }
        }
        return subjects; // 모든 과목 정보 반환
    }

    @Override
    public void save(SubjectDTO subjectDTO) throws SQLException {
        String query = "INSERT INTO subjects (subject_name, description, credit, created_at, updated_at, professor_user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, subjectDTO.getSubject_name());
            preparedStatement.setString(2, subjectDTO.getDescription());
            preparedStatement.setInt(3, subjectDTO.getCredit());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(subjectDTO.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(subjectDTO.getUpdatedAt()));
            preparedStatement.setInt(6, subjectDTO.getProfessorId() != null ? subjectDTO.getProfessorId().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(SubjectDTO subjectDTO) throws SQLException {
        String query = "UPDATE subjects SET subject_name = ?, description = ?, credit = ?, created_at = ?, updated_at = ?, professor_user_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, subjectDTO.getSubject_name());
            preparedStatement.setString(2, subjectDTO.getDescription());
            preparedStatement.setInt(3, subjectDTO.getCredit());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(subjectDTO.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(subjectDTO.getUpdatedAt()));
            preparedStatement.setInt(6, subjectDTO.getProfessorId() != null ? subjectDTO.getProfessorId().getId() : null);
            preparedStatement.setInt(7, subjectDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM subjects WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private SubjectDTO mapRowToSubjectDTO(ResultSet resultSet) throws SQLException {
        UserDTO professorDTO = null;
        Integer professorId = resultSet.getInt("professor_user_id");

        // 교수 ID가 null이 아닐 경우 교수 정보를 설정
        if (professorId != null) {
            professorDTO = UserDTO.builder()
                    .id(professorId)
                    .userName(resultSet.getString("professor_name")) // 교수 이름 설정
                    .build();
        }

        return SubjectDTO.builder()
                .id(resultSet.getInt("id"))
                .subject_name(resultSet.getString("subject_name"))
                .description(resultSet.getString("description"))
                .credit(resultSet.getInt("credit"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .professorId(professorDTO) // 교수 정보 추가
                .build();
    }

}
