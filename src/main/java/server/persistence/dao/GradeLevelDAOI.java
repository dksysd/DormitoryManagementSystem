package server.persistence.dao;

import server.persistence.dto.GradeLevelDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * GradeLevelDAOI 인터페이스는 데이터베이스에서 학년 레벨 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 학년 레벨 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface GradeLevelDAOI {

    /**
     * 주어진 ID를 기반으로 학년 레벨 데이터를 검색합니다.
     *
     * @param id 검색할 학년 레벨 데이터의 고유 ID
     * @return ID에 해당하는 {@link GradeLevelDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    GradeLevelDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 이름을 기반으로 학년 레벨 데이터를 검색합니다.
     *
     * @param name 검색할 학년 레벨 이름
     * @return 이름에 해당하는 {@link GradeLevelDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    GradeLevelDTO findByName(String name) throws SQLException;

    /**
     * 모든 학년 레벨 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 학년 레벨 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<GradeLevelDTO> findAll() throws SQLException;

    /**
     * 새로운 학년 레벨 데이터를 데이터베이스에 저장합니다.
     *
     * @param gradeLevelDTO 저장할 {@link GradeLevelDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(GradeLevelDTO gradeLevelDTO) throws SQLException;

    /**
     * 기존 학년 레벨 데이터를 업데이트합니다.
     *
     * @param gradeLevelDTO 업데이트할 {@link GradeLevelDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(GradeLevelDTO gradeLevelDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 학년 레벨 데이터를 삭제합니다.
     *
     * @param id 삭제할 학년 레벨 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}