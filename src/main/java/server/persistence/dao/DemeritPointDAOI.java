package server.persistence.dao;

import server.persistence.dto.DemeritPointDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * DemeritPointDAOI 인터페이스는 데이터베이스에서 벌점 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 벌점 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface DemeritPointDAOI {

    /**
     * 주어진 ID를 기반으로 벌점 데이터를 검색합니다.
     *
     * @param id 검색할 벌점 데이터의 고유 ID
     * @return ID에 해당하는 {@link DemeritPointDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    DemeritPointDTO findById(Integer id) throws SQLException;

    /**
     * 모든 벌점 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 벌점 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<DemeritPointDTO> findAll() throws SQLException;

    /**
     * 모든 벌점 데이터를 문자열 형식으로 반환합니다.
     *
     * @return 벌점 데이터를 설명하는 문자열 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<String> findAllPointIntoString() throws SQLException;

    /**
     * 새로운 벌점 데이터를 데이터베이스에 저장합니다.
     *
     * @param demeritPointDTO 저장할 {@link DemeritPointDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(DemeritPointDTO demeritPointDTO) throws SQLException;

    /**
     * 벌점 데이터를 UID, 설명, 점수 등의 정보를 기반으로 저장합니다.
     *
     * @param uid         벌점 데이터를 식별하는 사용자 고유 ID
     * @param description 벌점에 대한 설명
     * @param score       부여된 벌점 점수
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void savePoint(String uid, String description, int score) throws SQLException;

    /**
     * 기존 벌점 데이터를 업데이트합니다.
     *
     * @param demeritPointDTO 업데이트할 {@link DemeritPointDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(DemeritPointDTO demeritPointDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 벌점 데이터를 삭제합니다.
     *
     * @param id 삭제할 벌점 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}