package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.AddressDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO implements AddressDAOI{

    // ID를 입력받아 DB에서 데이터를 반환하는 메서드이다.
    @Override
    public AddressDTO findById(Integer id) throws SQLException {
        String query = "SELECT id, postal_name, do, si, detail_address, created_at FROM addresses WHERE id = ?";
        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowAddress(rs);
            }
        }
        return null;
    }

    // DB의 모든 항목들을 가져와서 List로 만든 뒤, 반환하는 메서드이다.
    @Override
    public List<AddressDTO> findAll() throws SQLException {
        List<AddressDTO> addresses = new ArrayList<>();
        String query = "SELECT * FROM addresses";

        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                addresses.add(mapRowAddress(rs));
            }
        }

        return addresses;
    }

    // 입력받은 데이터를 바탕으로 DB에 INSERT하는 메서드이다.
    @Override
    public void save(AddressDTO address) throws SQLException {
        String query = "insert into addresses (postal_name, do, si, detail_address, created_at) values (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, address.getPostalCode());
            ps.setString(2, address.get_do());
            ps.setString(3, address.getSi());
            ps.setString(4, address.getDetailAddress());
            ps.setTimestamp(5, Timestamp.valueOf(address.getCreatedAt()));
            ps.executeUpdate();
        }
    }

    // id를 제외하고 나머지 영역이 바뀌어있는 데이터를 가져와, 업데이트 하는 메서드이다.
    @Override
    public void update(AddressDTO address) throws SQLException {
        String query = "UPDATE addresses SET postal_name = ?, do = ?, si = ?, detail_address = ?, created_at = ? WHERE  id = ?";
        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, address.getPostalCode());
            ps.setString(2, address.get_do());
            ps.setString(3, address.getSi());
            ps.setString(4, address.getDetailAddress());
            ps.setTimestamp(5, Timestamp.valueOf(address.getCreatedAt()));
            ps.setInt(6, address.getId());
            ps.executeUpdate();
        }
    }

    // 입력받은 id를 DB에서 제거하는 메서드이다.
    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM addresses WHERE id = ?";
        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // 정해진 데이터 형태로 다시 매핑하는 메서드이다.
    private AddressDTO mapRowAddress(ResultSet rs) throws SQLException {
        return AddressDTO.builder()
                .id(rs.getInt("id"))
                .postalCode(rs.getString("postal_name"))
                ._do(rs.getString("do"))
                .si(rs.getString("si"))
                .detailAddress(rs.getString("detail_address"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}
