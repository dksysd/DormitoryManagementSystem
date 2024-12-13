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
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // 상태 이름을 입력받아 DB에서 데이터를 반환하는 메서드이다.
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

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
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

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
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

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
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

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM user_types WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private UserTypeDTO mapRowToUserTypeDTO(ResultSet rs) throws SQLException {
        UserTypeDTO userTypeDTO = UserTypeDTO.builder()
                .id(rs.getInt("id"))
                .typeName(rs.getString("type_name"))
                .description(rs.getString("description"))
                .build();

        return userTypeDTO;
    }
}
