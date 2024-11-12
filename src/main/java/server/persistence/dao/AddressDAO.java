package server.persistence.dao;

import server.config.DatabaseConnectionPool;
import server.persistence.dto.AddressDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
}
