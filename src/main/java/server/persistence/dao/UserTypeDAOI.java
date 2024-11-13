package server.persistence.dao;

import server.persistence.dto.BankTransferPaymentDTO;
import server.persistence.dto.UserTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserTypeDAOI {
    UserTypeDTO findById(Integer id) throws SQLException;
    UserTypeDTO findByTypeName(String typeName) throws SQLException;
    public List<UserTypeDTO> findAll() throws SQLException;
    public void save(UserTypeDTO userTypeDTO) throws SQLException;
    public void update(UserTypeDTO userTypeDTO) throws SQLException;
    public void delete(Integer id) throws SQLException;
}
