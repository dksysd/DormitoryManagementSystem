package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.AddressDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO implements AddressDAOI{
    @Override
    public AddressDTO findById(Integer id) throws SQLException {
        String query = "SELECT * FROM addresses WHERE id = ?";
        Connection con = DatabaseConnectionPool.getConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return null;
        }

        Integer addressId = rs.getInt("id");
        String postalCode = rs.getString("postal_code");
        String _do = rs.getString("do");
        String si = rs.getString("si");
        String detailAddress = rs.getString("detail_address");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return AddressDTO.builder()
                .id(addressId)
                .postalCode(postalCode)
                ._do(_do)
                .si(si)
                .detailAddress(detailAddress)
                .createdAt(createdAt)
                .build();
    }

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

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM addresses WHERE id = ?";
        try (Connection con = DatabaseConnectionPool.getConnection();
            PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private AddressDTO mapRowAddress(ResultSet rs) throws SQLException {
        AddressDTO dto = AddressDTO.builder()
                .id(rs.getInt("id"))
                .postalCode(rs.getString("postal_code"))
                ._do(rs.getString("do"))
                .si(rs.getString("si"))
                .detailAddress(rs.getString("detail_address"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();

        return dto;
    }
}
