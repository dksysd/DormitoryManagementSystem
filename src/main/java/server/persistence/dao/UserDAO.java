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
        String query = "SELECT u.id, u.uid, u.login_id, u.user_name, u.phone_number, u.created_at, u.updated_at, " +
                "u.user_type_id, u.gender_code_id, u.address_id, u.profile_image " +
                "ut.type_name AS user_type_name, gc.gender_code AS gender_code, gc.code_name AS code_name, a.address AS user_address " +
                "a.postal_code AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address " +
                "i.name AS image_name, i.extension AS extension" +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id " +
                "LEFT JOIN images i ON u.profile_image = i.id" +
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
        String query = "SELECT u.id, u.uid, u.login_id, u.user_name, u.phone_number, u.created_at, u.updated_at, " +
                "u.user_type_id, u.gender_code_id, u.address_id, u.profile_image " +
                "ut.type_name AS user_type_name, gc.gender_code AS gender_code, gc.code_name AS code_name, a.address AS user_address " +
                "a.postal_code AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address " +
                "i.name AS image_name, i.extension AS extension" +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id " +
                "LEFT JOIN images i ON u.profile_image = i.id" +
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
    public List<UserDTO> findAll() throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        String query = "SELECT u.id, u.uid, u.login_id, u.user_name, u.phone_number, u.created_at, u.updated_at, " +
                "u.user_type_id, u.gender_code_id, u.address_id, u.profile_image " +
                "ut.type_name AS user_type_name, gc.gender_code AS gender_code, gc.code_name AS code_name, a.address AS user_address " +
                "a.postal_code AS postal_code, a.do AS adress_do, a.si AS adress_si, a.detail_address AS detail_address " +
                "i.name AS image_name, i.extension AS extension" +
                "FROM users u " +
                "LEFT JOIN user_types ut ON u.user_type_id = ut.id " +
                "LEFT JOIN gender_codes gc ON u.gender_code_id = gc.id " +
                "LEFT JOIN addresses a ON u.address_id = a.id" +
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
        String query = "INSERT INTO users (uid, login_id, user_name, phone_number, created_at, updated_at, user_type_id, gender_code_id, address_id, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userDTO.getUid());
            preparedStatement.setString(2, userDTO.getLoginId());
            preparedStatement.setString(3, userDTO.getUserName());
            preparedStatement.setString(4, userDTO.getPhoneNumber());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(userDTO.getCreatedAt()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(userDTO.getUpdatedAt()));
            preparedStatement.setInt(7, userDTO.getUserTypeDTO() != null ? userDTO.getUserTypeDTO().getId() : null);
            preparedStatement.setInt(8, userDTO.getGenderCodeDTO() != null ? userDTO.getGenderCodeDTO().getId() : null);
            preparedStatement.setInt(9, userDTO.getAddressDTO() != null ? userDTO.getAddressDTO().getId() : null);
            preparedStatement.setInt(10, userDTO.getImageDTO() != null ? userDTO.getImageDTO().getId() : null);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(UserDTO userDTO) throws SQLException {
        String query = "UPDATE users SET uid = ?, login_id = ?, user_name = ?, phone_number = ?, created_at = ?, updated_at = ?, user_type_id = ?, gender_code_id = ?, address_id = ?,profile_image = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userDTO.getUid());
            preparedStatement.setString(2, userDTO.getLoginId());
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
                .loginId(resultSet.getString("login_id"))
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
