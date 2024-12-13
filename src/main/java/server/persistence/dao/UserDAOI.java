package server.persistence.dao;

import server.persistence.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * UserDAOI 인터페이스는 데이터베이스에서 사용자 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 사용자 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface UserDAOI {

    /**
     * 주어진 ID를 기반으로 사용자 데이터를 검색합니다.
     *
     * @param id 검색할 사용자 데이터의 고유 ID
     * @return 주어진 ID에 해당하는 {@link UserDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    UserDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 Uid를 기반으로 사용자 데이터를 검색합니다.
     *
     * @param uid 검색할 사용자의 Uid
     * @return 주어진 Uid에 해당하는 {@link UserDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    UserDTO findByUid(String uid) throws SQLException;

    /**
     * 모든 사용자 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 사용자 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<UserDTO> findAll() throws SQLException;

    /**
     * 특정 사용자의 퇴실 상태 데이터를 확인합니다.
     *
     * @param uid 확인할 사용자의 Uid
     * @return 퇴실 상태를 나타내는 문자열
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    String checkMoveOut(String uid) throws SQLException;

    /**
     * 특정 조건을 충족하는 사용자 Uid 목록을 검색합니다.
     *
     * @return 조건을 충족하는 사용자 Uid 문자열 목록
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<String> findAllOfSelection() throws SQLException;

    /**
     * 새로운 사용자 데이터를 데이터베이스에 저장합니다.
     *
     * @param userDTO 저장할 {@link UserDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 동안 오류가 발생한 경우
     */
    void save(UserDTO userDTO) throws SQLException;

    /**
     * 기존의 사용자 데이터를 업데이트합니다.
     *
     * @param userDTO 업데이트할 {@link UserDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void update(UserDTO userDTO) throws SQLException;

    /**
     * 사용자의 룸메이트 정보를 업데이트합니다.
     *
     * @param uid      업데이트할 사용자 Uid
     * @param roommate 설정할 룸메이트 Uid
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void updateRoommate(String uid, String roommate) throws SQLException;

    /**
     * 주어진 ID에 해당하는 사용자 데이터를 삭제합니다.
     *
     * @param id 삭제할 사용자 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 동안 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}