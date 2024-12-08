//package server.controller;
//
//import server.network.protocol.Body;
//import server.network.protocol.Header;
//import server.network.protocol.TLV.Protocol;
//import server.persistence.dao.UserDAO;
//import server.persistence.dao.UserDAOI;
//import server.persistence.dto.LoginDTO;
//import server.persistence.dto.UserDTO;
//
//import java.sql.SQLException;
//import java.util.Objects;
//
//public class UserController {
//
//    public UserDTO getUserInfo(Protocol protocol) {
//        // 수신한 Protocol의 Header 확인
//        Header header = protocol.getHeader();
//
//        // 요청 코드 확인
//        if (header.getCode() != Code.RequestCode.LOGIN) {
//            throw new IllegalArgumentException("Invalid Request Code");
//        }
//
//        // 내부 Protocol에서 사용자 정보 추출
//        Protocol innerProtocol1 = protocol.getInnerProtocol(0); // 첫 번째 내부 Protocol
//        Protocol innerProtocol2 = protocol.getInnerProtocol(1); // 두 번째 내부 Protocol
//
//        // ID와 PASSWORD 데이터를 추출
//        String id = new String(innerProtocol1.getData());
//        String password = new String(innerProtocol2.getData());
//
//
//
//}
//
//
//    private static boolean User(LoginDTO loginDTO, Body responseBody) throws SQLException {
//        UserDAOI userDAO = new UserDAO() {
//        };
//        String id = loginDTO.getId();
//        UserDTO userDTO = userDAO.findByUid(id);
//        if (userDTO == null) {
//            responseBody.addData("error", "user not found");
//            return false;
//        }
//        if(!Objects.equals(loginDTO.getPassword(), userDAO.getPasswordByUid(id))){
//            responseBody.addData("error", "password does not match");
//            return false;
//        }
//        return true;
//    }