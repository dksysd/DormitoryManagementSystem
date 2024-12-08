package server.controller;

import server.network.protocol.Body;
import server.network.protocol.Header;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;
import server.persistence.dao.UserDAO;
import server.persistence.dto.LoginDTO;
import server.persistence.dto.UserDTO;
import java.sql.SQLException;
import java.util.Objects;

public class AuthController implements Controller {
    /**
     * id : 숫자 8자리 제한, password: 8~20자리, 영어, 특수문자, 숫자 포함
     *
     * @param requestProtocol RequestProtocol 내에 id,pw 등의 정보를 loginDTO 객체로 전송 요망
     * @param response        header(contentType: APPLICATION_JSON)
     */
    public static void login(RequestProtocol requestProtocol, ResponseProtocol response) {
        try {

            Header header = response.getHeader();
            header.setContentType(Header.ContentType.APPLICATION_JSON);
            Body responseBody = response.getBody();
            Body requestBody = requestProtocol.getBody();
            LoginDTO loginDTO = (LoginDTO) requestBody.getData("loginDTO");

            if (!isValidateLoginDTO(loginDTO, response, responseBody)) {
                return;
            } else if (!isValidLoginCredentials(loginDTO, requestBody)) {
                response.setStatus(ResponseProtocol.Status.Unauthorized);
                return;
            }
            // 최종 응답 설정
            response.setStatus(ResponseProtocol.Status.OK);
            responseBody.addData("message", "Login successful");

        } catch (Exception e) {
            response.setStatus(ResponseProtocol.Status.InternalServerError);
        }
    }
    public static void logout(RequestProtocol requestProtocol, ResponseProtocol response) {
        try {
            Header header = response.getHeader();
            header.setContentType(Header.ContentType.APPLICATION_JSON);
            Body responseBody = response.getBody();

            // 로그아웃 성공 응답 설정
            response.setStatus(ResponseProtocol.Status.OK);
            responseBody.addData("message", "Logout successful");

        } catch (Exception e) {
            response.setStatus(ResponseProtocol.Status.InternalServerError);
            response.getBody().addData("error", "Failed to process logout");
        }
    }

    public static boolean isValidateLoginDTO(LoginDTO loginDTO, ResponseProtocol response, Body responseBody) {
        try {
            Objects.requireNonNull(loginDTO, "loginDTO is null");
            Objects.requireNonNull(loginDTO.getId(), "loginDTO.id is null");
            Objects.requireNonNull(loginDTO.getPassword(), "loginDTO.password is null");
        } catch (NullPointerException e) {
            response.setStatus(ResponseProtocol.Status.BadRequest);
            responseBody.addData("error", e.getMessage());  // 예외 메시지를 반환
            return false;  // 검증 실패 시 false 반환
        }
        return true;  // 검증 성공 시 true 반환
    }

    private static boolean isValidLoginCredentials(LoginDTO loginDTO, Body responseBody) throws SQLException {
        String id = loginDTO.getId();
        String password = loginDTO.getPassword();

        // id와 password null 체크
        if (id == null) {
            responseBody.addData("error", "id is null");
            return false;
        } else if (password == null) {
            responseBody.addData("error", "password is null");
            return false;
        }

        // ID 형식 검증 (8자리 숫자)
        if (!isValidId(id)) {
            responseBody.addData("error", "id is invalid");
            return false;
        }
        if (!isValidPassword(password)) {
            responseBody.addData("error", "password is invalid");
            return false;
        }

        // 실제 데이터베이스 검증
        return authenticateUser(loginDTO, responseBody);
    }

    private static boolean isValidId(String id) {
        // id가 8자리인지 확인(id는 학번)
        if (id.length() != 8) {
            return false;
        }
        // 모든 문자가 숫자인지 확인
        try {
            Long.parseLong(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidPassword(String password) {
        // 길이 검증 (8-20자)
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        int validationCount = 0;

        // 소문자 포함 여부 검사
        if (password.matches(".*[a-z].*") || password.matches(".*[A-Z].*")) {
            validationCount++;
        }

        // 숫자 포함 여부 검사
        if (password.matches(".*\\d.*")) {
            validationCount++;
        }

        // 특수문자 포함 여부 검사
        if (password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?].*")) {
            validationCount++;
        }

        // 3가지 이상의 조합이 있어야 함
        return validationCount >= 3;
    }

    private static boolean authenticateUser(LoginDTO loginDTO, Body responseBody) throws SQLException {
        UserDAO userDAO = new UserDAO() {
        };
        String id = loginDTO.getId();
        UserDTO userDTO = userDAO.findByUid(id);
        if (userDTO == null) {
            responseBody.addData("error", "user not found");
            return false;
        }
        if(!Objects.equals(loginDTO.getPassword(), userDAO.getPasswordByUid(id))){
            responseBody.addData("error", "password does not match");
            return false;
        }
        return true;
    }
}