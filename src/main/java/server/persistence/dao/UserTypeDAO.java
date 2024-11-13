package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.UserTypeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
