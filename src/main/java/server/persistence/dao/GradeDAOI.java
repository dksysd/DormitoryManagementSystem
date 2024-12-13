package server.persistence.dao;

import server.persistence.dto.GradeDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * GradeDAOI 인터페이스는 데이터베이스에서 성적 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 성적 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface GradeDAOI {

    /**
     * 주어진 ID를 기반으로 성적 데이터를 검색합니다.
     *
     * @param id 검색할 성적 데이터의 고유 ID
     * @return ID에 해당하는 {@link GradeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    GradeDTO findById(Integer id) throws SQLException;

    /**
     * 모든 성적 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 성적 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<GradeDTO> findAll() throws SQLException;

    /**
     * 새로운 성적 데이터를 데이터베이스에 저장합니다.
     *
     * @param gradeDTO 저장할 {@link GradeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(GradeDTO gradeDTO) throws SQLException;

    /**
     * 기존 성적 데이터를 업데이트합니다.
     *
     * @param gradeDTO 업데이트할 {@link GradeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(GradeDTO gradeDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 성적 데이터를 삭제합니다.
     *
     * @param id 삭제할 성적 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}