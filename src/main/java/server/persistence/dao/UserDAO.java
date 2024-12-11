package server.persistence.dao;

import server.persistence.dto.*;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements UserDAOI {
    @Override
    public UserDTO findById(Integer id) throws SQLException {
        String query = "SELECT u.id, u.uid, u.login_password, u.user_name, u.phone_number, u.created_at, u.updated_at, " +
                "u.user_type_id, u.gender_code_id, u.address_id, u.profile_image," +
                "ut.type_name AS user_type_name, gc.code_name AS gender_code, gc.code_name AS code_name, a.detail_address AS user_address " +
                ",a.postal_name AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address," +
                "i.name AS image_name, i.extension AS extension " +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id " +
                "LEFT JOIN images i ON u.profile_image = i.id " +
                "WHERE u.id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUserDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    @Override
    public UserDTO findByUid(String uid) throws SQLException {
        String query = "SELECT u.id AS id, u.uid AS uid, u.login_password AS login_password, u.user_name AS user_name, " +
                "u.phone_number AS phone_number, u.created_at AS created_at, u.updated_at AS updated_at, " +
                "u.user_type_id AS user_type_id, u.gender_code_id AS gender_code_id, u.address_id AS address_id" +
                ", u.profile_image AS profile_image, " +
                "ut.type_name AS user_type_name, gc.code_name AS gender_code, gc.code_name AS code_name, a.detail_address AS user_address, " +
                "a.postal_name AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address, " +
                "i.name AS image_name, i.extension AS extension " +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id " +
                "LEFT JOIN images i ON u.profile_image = i.id " +
                "WHERE u.uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, uid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToUserDTO(resultSet);
            }
        }
        return null;
    }
    @Override
    public List<String> findAllOfSelection() throws SQLException {
        List<String> users = new ArrayList<>();
        String query = "SELECT u.uid AS uid FROM users u " +
                "INNER JOIN selection_applications sa ON sa.user_id = u.id " +
                "INNER JOIN selection_application_statuses sas ON sas.id = sa.selection_application_status_id " +
                "WHERE sas.status_name = " + "'선발대기'";
        try(Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }
        }

        return users;
    }
    @Override
    public List<UserDTO> findAll() throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        String query = "SELECT u.id, u.uid, u.login_password, u.user_name, u.phone_number, u.created_at, u.updated_at, " +
                "u.user_type_id, u.gender_code_id, u.address_id, u.profile_image," +
                "ut.type_name AS user_type_name, gc.code_name AS gender_code, gc.code_name AS code_name, a.detail_address AS user_address," +
                "a.postal_name AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address," +
                "i.name AS image_name, i.extension AS extension " +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id " +
                "LEFT JOIN images i ON u.profile_image = i.id";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRowToUserDTO(resultSet));
            }
        }
        return users; // 모든 사용자 정보 반환
    }

    @Override
    public void save(UserDTO userDTO) throws SQLException {
        String query = "INSERT INTO users (uid, login_password, salt, user_name, phone_number, created_at, updated_at, user_type_id, gender_code_id, address_id, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userDTO.getUid());
            preparedStatement.setString(2, userDTO.getLoginPassword());
            preparedStatement.setString(3,"abc");
            preparedStatement.setString(4, userDTO.getUserName());
            preparedStatement.setString(5, userDTO.getPhoneNumber());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(userDTO.getCreatedAt()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(userDTO.getUpdatedAt()));
            preparedStatement.setInt(8, userDTO.getUserTypeDTO() != null ? userDTO.getUserTypeDTO().getId() : null);
            preparedStatement.setInt(9, userDTO.getGenderCodeDTO() != null ? userDTO.getGenderCodeDTO().getId() : null);
            preparedStatement.setInt(10, userDTO.getAddressDTO() != null ? userDTO.getAddressDTO().getId() : null);
            preparedStatement.setInt(11, userDTO.getImageDTO() != null ? userDTO.getImageDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(UserDTO userDTO) throws SQLException {
        String query = "UPDATE users SET uid = ?, login_password = ?, user_name = ?, phone_number = ?, created_at = ?, updated_at = ?, user_type_id = ?, gender_code_id = ?, address_id = ?,profile_image = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userDTO.getUid());
            preparedStatement.setString(2, userDTO.getLoginPassword());
            preparedStatement.setString(3, userDTO.getUserName());
            preparedStatement.setString(4, userDTO.getPhoneNumber());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(userDTO.getCreatedAt()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(userDTO.getUpdatedAt()));
            preparedStatement.setInt(7, userDTO.getUserTypeDTO() != null ? userDTO.getUserTypeDTO().getId() : null);
            preparedStatement.setInt(8, userDTO.getGenderCodeDTO() != null ? userDTO.getGenderCodeDTO().getId() : null);
            preparedStatement.setInt(9, userDTO.getAddressDTO() != null ? userDTO.getAddressDTO().getId() : null);
            preparedStatement.setInt(10, userDTO.getId());
            preparedStatement.setInt(11,userDTO.getImageDTO()!= null ? userDTO.getImageDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateRoommate(String uid, String roommate) throws SQLException {
        String query = "SELECT sa.id AS id, u.id AS userId, sa.roommate_user_id AS roommate_id FROM users u " +
                "INNER JOIN selection_applications sa ON u.id = sa.user_id " +
                "WHERE u.uid = ?";
        int id;
        int userId;
        int roommateId;

        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1,uid);
            ResultSet resultSet = preparedStatement.executeQuery();

            id = resultSet.getInt("id");
            userId = resultSet.getInt("userId");
        }

        query = "SELECT id FROM users WHERE uid = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, roommate);

            ResultSet resultSet = preparedStatement.executeQuery();

            roommateId = resultSet.getInt(1);
        }

        query = "UPDATE selection_applications sa SET roommate_user_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, roommateId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }

        query = "UPDATE selection_applications sa SET roommate_user_id = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roommateId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private UserDTO mapRowToUserDTO(ResultSet resultSet) throws SQLException {
        UserTypeDTO userTypeDTO = UserTypeDTO.builder()
                .id(resultSet.getInt("user_type_id"))
                .typeName(resultSet.getString("user_type_name")) // 사용자 유형 이름 추가
                .build();

        GenderCodeDTO genderCodeDTO = GenderCodeDTO.builder()
                .id(resultSet.getInt("gender_code_id"))
                .codeName(resultSet.getString("code_name")) // 성별 코드 추가
                .build();

        AddressDTO addressDTO = AddressDTO.builder()
                .id(resultSet.getInt("address_id"))
                .postalCode(resultSet.getString("postal_code")) // 주소 추가
                ._do(resultSet.getString("address_do"))
                .si(resultSet.getString("address_si"))
                .detailAddress(resultSet.getString("detail_address"))
                .build();

        ImageDTO imageDTO = ImageDTO.builder()
                .id(resultSet.getInt("profile_image"))
                .name(resultSet.getString("image_name"))
                .extension(resultSet.getString("image_extension"))
                .build();

        return UserDTO.builder()
                .id(resultSet.getInt("id"))
                .uid(resultSet.getString("uid"))
                .loginPassword(resultSet.getString("login_password"))
                .userName(resultSet.getString("user_name"))
                .phoneNumber(resultSet.getString("phone_number"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                .userTypeDTO(userTypeDTO) // 사용자 유형 DTO 추가
                .genderCodeDTO(genderCodeDTO) // 성별 코드 DTO 추가
                .addressDTO(addressDTO)
                .imageDTO(imageDTO)// 주소 DTO 추가
                .build();
    }

}
