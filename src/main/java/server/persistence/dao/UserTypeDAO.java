package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.UserTypeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTypeDAO implements UserTypeDAOI {
    @Override
    public UserTypeDTO findById(Integer id) throws SQLException {
        String query = "select * from user_types where id = ?";
        Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }

        Integer userTypeId = resultSet.getInt("id");
        String typeName = resultSet.getString("type_name");
        String description = resultSet.getString("description");
        return UserTypeDTO.builder()
                .id(userTypeId)
                .typeName(typeName)
                .description(description)
                .build();
    }

    @Override
    public UserTypeDTO findByTypeName(String typeName) throws SQLException {
        String query = "select * from user_types where type_name = ?";
        Connection connection = DatabaseConnectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, typeName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }

        Integer id = resultSet.getInt("id");
        String userTypeName = resultSet.getString("type_name");
        String description = resultSet.getString("description");
        return UserTypeDTO.builder()
                .id(id)
                .typeName(userTypeName)
                .description(description)
                .build();
    }

    @Override
    public List<UserTypeDTO> findAll() throws SQLException {
        List<UserTypeDTO> userTypes = new ArrayList<>();
        String query = "select * from user_types";

        try(Connection connection = DatabaseConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                userTypes.add(mapRowToUserTypeDTO(resultSet));
            }
        }

        return userTypes;
    }

    @Override
    public void save(UserTypeDTO userTypeDTO) throws SQLException {
        String query = "insert into user_types(type_name, description) values(?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userTypeDTO.getTypeName());
            preparedStatement.setString(2, userTypeDTO.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(UserTypeDTO userTypeDTO) throws SQLException {
        String query = "update user_types set type_name = ?, description = ? where id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, userTypeDTO.getTypeName());
            preparedStatement.setString(2, userTypeDTO.getDescription());
            preparedStatement.setInt(3, userTypeDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM user_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }
    }

    private UserTypeDTO mapRowToUserTypeDTO(ResultSet rs) throws SQLException {
        UserTypeDTO userTypeDTO = UserTypeDTO.builder()
                .id(rs.getInt("id"))
                .typeName(rs.getString("type_name"))
                .description(rs.getString("description"))
                .build();

        return userTypeDTO;
    }
}
