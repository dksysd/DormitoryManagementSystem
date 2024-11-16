package server.persistence.dao;

import server.persistence.dto.ImageDTO;
import server.config.DatabaseConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements ImageDAOI {

    @Override
    public ImageDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, name, data, width, height, extension FROM images WHERE id = ?";
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

    @Override
    public List<ImageDTO> findAll() throws SQLException {
        List<ImageDTO> images = new ArrayList<>();
        String query = "SELECT id, name, data, width, height, extension FROM images";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                images.add(mapRowToImageDTO(resultSet));
            }
        }
        return images; // 모든 이미지 정보 반환
    }

    @Override
    public void save(ImageDTO imageDTO) throws SQLException {
        String query = "INSERT INTO images (name, data, width, height, extension) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, imageDTO.getName());
            preparedStatement.setBytes(2, toPrimitiveByteArray(imageDTO.getData())); // Byte[]를 byte[]로 변환
            preparedStatement.setInt(3, imageDTO.getWidth());
            preparedStatement.setInt(4, imageDTO.getHeight());
            preparedStatement.setString(5, imageDTO.getExtension());
            preparedStatement.executeUpdate();
        }
    }

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

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM images WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private ImageDTO mapRowToImageDTO(ResultSet resultSet) throws SQLException {
        return ImageDTO.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .data(toObjectByteArray(resultSet.getBytes("data"))) // byte[]를 Byte[]로 변환
                .width(resultSet.getInt("width"))
                .height(resultSet.getInt("height"))
                .extension(resultSet.getString("extension"))
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
