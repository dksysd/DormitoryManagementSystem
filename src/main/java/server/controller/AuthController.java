package server.controller;

import server.network.protocol.Body;
import server.network.protocol.Header;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;
import server.persistence.dao.UserDAOI;
import server.persistence.dto.LoginDTO;
import server.persistence.dto.UserDTO;

public class AuthController implements Controller {
    public static void login(RequestProtocol requestProtocol, ResponseProtocol response) {
        try {
            // 요청 메소드 검증
            if (requestProtocol.getMethod() != RequestProtocol.Method.POST) {
                response.setStatus(ResponseProtocol.Status.MethodNotAllowed);

            }

            // 요청 바디 검증
            Body requestBody = requestProtocol.getBody();
            if (requestBody == null || !requestBody.hasData("loginDTO")) {
                response.setStatus(ResponseProtocol.Status.BadRequest);

            }

            // LoginDTO 추출 및 검증
            LoginDTO loginDTO = (LoginDTO) requestBody.getData("loginDTO");
            if (loginDTO == null || loginDTO.getId() == null || loginDTO.getPassword() == null) {
                response.setStatus(ResponseProtocol.Status.BadRequest);

            }

            // 로그인 정보 검증
            if (!isValidLoginCredentials(loginDTO, response)) {
                response.setStatus(ResponseProtocol.Status.Unauthorized);

            }
            // 최종 응답 설정
            response.setStatus(ResponseProtocol.Status.OK);
            Header header = response.getHeader();
            header.setContentType(Header.ContentType.APPLICATION_JSON);
            Body body = response.getBody();
            body.addData("message", "Login successful");


        } catch (Exception e) {
            response.setStatus(ResponseProtocol.Status.InternalServerError);

        }


    }

    private static boolean isValidLoginCredentials(LoginDTO loginDTO, ResponseProtocol response) {
        // 1. null 체크
        if (loginDTO == null) {
            return false;
        }

        String id = loginDTO.getId();
        String password = loginDTO.getPassword();

        // 2. id와 password null 체크
        if (id == null || password == null) {
            return false;
        }

        // 3. ID 형식 검증 (8자리 숫자)
        if (!isValidId(id)) {
            return false;
        }
        if (!isValidPassword(password)) {
            return false;
        }

        // 4. 실제 데이터베이스 검증
        return authenticateUser(loginDTO, response);
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
        if (password.matches(".*[a-z].*")) {
            validationCount++;
        }

        // 대문자 포함 여부 검사
        if (password.matches(".*[A-Z].*")) {
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

    private static boolean authenticateUser(LoginDTO loginDTO, ResponseProtocol response) {
        UserDAOI userDAOI = new UserDAOI() {
        };
        UserDTO userDTO = userDAOI.findByUid(loginDTO.getId());
        if (userDTO == null) {
            return false;
        }
        return userDTO.getId().equals(loginDTO.getId());
    }
}