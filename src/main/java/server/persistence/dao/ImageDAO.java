package server.persistence.dao;

import server.persistence.dto.ImageDTO;
import server.config.DatabaseConnectionPool;
import server.persistence.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements ImageDAOI {
    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public ImageDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, name, data, width, height, extension, user_id FROM images WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mapRowToImageDTO(resultSet);
            }
        }
        return null; // ID에 해당하는 데이터가 없으면 null 반환
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<ImageDTO> findAll() throws SQLException {
        List<ImageDTO> images = new ArrayList<>();
        String query = "SELECT id, name, data, width, height, extension, user_id FROM images";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                images.add(mapRowToImageDTO(resultSet));
            }
        }
        return images; // 모든 이미지 정보 반환
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public int save(ImageDTO imageDTO) throws SQLException {
        int id;
        String query = "INSERT INTO images (name, data, width, height, extension) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, imageDTO.getName());
            preparedStatement.setBytes(2, toPrimitiveByteArray(imageDTO.getData())); // Byte[]를 byte[]로 변환
            preparedStatement.setInt(3, imageDTO.getWidth());
            preparedStatement.setInt(4, imageDTO.getHeight());
            preparedStatement.setString(5, imageDTO.getExtension());
            preparedStatement.executeUpdate();

            id = preparedStatement.getGeneratedKeys().getInt(1);
        }
        return id;
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(ImageDTO imageDTO) throws SQLException {
        String query = "UPDATE images SET name = ?, data = ?, width = ?, height = ?, extension = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, imageDTO.getName());
            preparedStatement.setBytes(2, toPrimitiveByteArray(imageDTO.getData())); // Byte[]를 byte[]로 변환
            preparedStatement.setInt(3, imageDTO.getWidth());
            preparedStatement.setInt(4, imageDTO.getHeight());
            preparedStatement.setString(5, imageDTO.getExtension());
            preparedStatement.setInt(6, imageDTO.getId());
            preparedStatement.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM images WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private ImageDTO mapRowToImageDTO(ResultSet resultSet) throws SQLException {
        UserDAO dao = new UserDAO();
        UserDTO userDTO = dao.findById(resultSet.getInt("user_id"));

        return ImageDTO.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .data(toObjectByteArray(resultSet.getBytes("data"))) // byte[]를 Byte[]로 변환
                .width(resultSet.getInt("width"))
                .height(resultSet.getInt("height"))
                .extension(resultSet.getString("extension"))
                .userDTO(userDTO)
                .build();
    }

    // Byte[]를 byte[]로 변환
    private byte[] toPrimitiveByteArray(Byte[] byteObjArray) {
        if (byteObjArray == null) {
            return null;
        }
        byte[] byteArray = new byte[byteObjArray.length];
        for (int i = 0; i < byteObjArray.length; i++) {
            byteArray[i] = byteObjArray[i]; // Unboxing
        }
        return byteArray;
    }

    // byte[]를 Byte[]로 변환
    private Byte[] toObjectByteArray(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        Byte[] byteObjArray = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            byteObjArray[i] = byteArray[i]; // Boxing
        }
        return byteObjArray;
    }
}
