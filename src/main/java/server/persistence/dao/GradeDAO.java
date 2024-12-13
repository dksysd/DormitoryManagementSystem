package server.persistence.dao;

import server.persistence.dto.GradeDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.GradeLevelDTO;
import server.persistence.dto.SubjectDTO;
import server.persistence.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO implements GradeDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public GradeDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, created_at, updated_at, subject_id, student_user_id, grade_level_id FROM grades WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToGradeDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<GradeDTO> findAll() throws SQLException {
        List<GradeDTO> grades = new ArrayList<>();
        String query = "SELECT id, created_at, updated_at, subject_id, student_user_id, grade_level_id FROM grades";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                grades.add(mapRowToGradeDTO(resultSet));
            }
        }
        return grades; // 모든 성적 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(GradeDTO gradeDTO) throws SQLException {
        String query = "INSERT INTO grades (created_at, updated_at, subject_id, student_user_id, grade_level_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(gradeDTO.getCreatedAt()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(gradeDTO.getUpdatedAt()));
            preparedStatement.setInt(3, gradeDTO.getSubjectDTO() != null ? gradeDTO.getSubjectDTO().getId() : null);
            preparedStatement.setInt(4, gradeDTO.getUserDTO() != null ? gradeDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(5, gradeDTO.getGradeLevelDTO() != null ? gradeDTO.getGradeLevelDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(GradeDTO gradeDTO) throws SQLException {
        String query = "UPDATE grades SET created_at = ?, updated_at = ?, subject_id = ?, student_user_id = ?, grade_level_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(gradeDTO.getCreatedAt()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(gradeDTO.getUpdatedAt()));
            preparedStatement.setInt(3, gradeDTO.getSubjectDTO() != null ? gradeDTO.getSubjectDTO().getId() : null);
            preparedStatement.setInt(4, gradeDTO.getUserDTO() != null ? gradeDTO.getUserDTO().getId() : null);
            preparedStatement.setInt(5, gradeDTO.getGradeLevelDTO() != null ? gradeDTO.getGradeLevelDTO().getId() : null);
            preparedStatement.setInt(6, gradeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM grades WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private GradeDTO mapRowToGradeDTO(ResultSet resultSet) throws SQLException {
        SubjectDAO dao1 = new SubjectDAO();
        SubjectDTO subjectDTO = dao1.findById(resultSet.getInt("subject_id"));
        UserDAO dao2 = new UserDAO();
        UserDTO userDTO = dao2.findById(resultSet.getInt("student_user_id"));
        GradeLevelDAO dao3 = new GradeLevelDAO();
        GradeLevelDTO gradeLevelDTO = dao3.findById(resultSet.getInt("grade_level_id"));

        return GradeDTO.builder()
                .id(resultSet.getInt("id"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .subjectDTO(subjectDTO)
                .userDTO(userDTO)
                .gradeLevelDTO(gradeLevelDTO)
                .build();
    }
}
