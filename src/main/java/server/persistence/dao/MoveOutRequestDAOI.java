package server.persistence.dao;

import server.persistence.dto.MoveOutRequestDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * MoveOutRequestDAOI 인터페이스는 데이터베이스에서 퇴실 요청 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 퇴실 요청 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface MoveOutRequestDAOI {

    /**
     * 주어진 ID를 기반으로 퇴실 요청 데이터를 검색합니다.
     *
     * @param id 검색할 퇴실 요청 데이터의 고유 ID
     * @return ID에 해당하는 {@link MoveOutRequestDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    MoveOutRequestDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 사용자 UID를 기반으로 퇴실 요청 데이터를 검색합니다.
     *
     * @param uid 검색할 사용자의 UID (고유 식별자)
     * @return UID에 해당하는 {@link MoveOutRequestDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    MoveOutRequestDTO findByUid(String uid) throws SQLException;

    /**
     * 모든 퇴실 요청 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 퇴실 요청 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<MoveOutRequestDTO> findAll() throws SQLException;

    /**
     * 퇴실 요청에 대한 특정 데이터를 모두 문자열 형태로 반환합니다.
     * <p>
     * 예를 들어, 사용자의 UID와 기숙사 이름 등 특정 필드를 문자열로 반환할 수 있습니다.
     *
     * @return 퇴실 요청 데이터의 특정 정보를 포함한 문자열 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<String> findAllOfMoveOut() throws SQLException;

    /**
     * 새로운 퇴실 요청 데이터를 데이터베이스에 저장합니다.
     *
     * @param moveOutRequestDTO 저장할 {@link MoveOutRequestDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 동안 오류가 발생한 경우
     */
    void save(MoveOutRequestDTO moveOutRequestDTO) throws SQLException;

    /**
     * 기존의 퇴실 요청 데이터를 업데이트합니다.
     *
     * @param moveOutRequestDTO 업데이트할 {@link MoveOutRequestDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void update(MoveOutRequestDTO moveOutRequestDTO) throws SQLException;

    /**
     * 주어진 UID와 상태를 기반으로 퇴실 요청의 상태를 업데이트합니다.
     *
     * @param uid    상태를 업데이트할 사용자의 UID
     * @param status 업데이트할 상태 값
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void updateStatus(String uid, String status) throws SQLException;

    /**
     * 주어진 ID에 해당하는 퇴실 요청 데이터를 삭제합니다.
     *
     * @param id 삭제할 퇴실 요청 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 동안 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}