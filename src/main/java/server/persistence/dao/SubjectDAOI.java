package server.persistence.dao;

import server.persistence.dto.SubjectDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * SubjectDAOI 인터페이스는 데이터베이스에서 과목 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 과목 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface SubjectDAOI {

    /**
     * 주어진 ID를 기반으로 과목 데이터를 검색합니다.
     *
     * @param id 검색할 과목 데이터의 고유 ID
     * @return 주어진 ID에 해당하는 {@link SubjectDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    SubjectDTO findById(Integer id) throws SQLException;

    /**
     * 과목 이름을 기준으로 과목 데이터를 검색합니다.
     *
     * @param name 검색할 과목 이름
     * @return 주어진 이름에 해당하는 {@link SubjectDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    SubjectDTO findByName(String name) throws SQLException;

    /**
     * 모든 과목 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 과목 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<SubjectDTO> findAll() throws SQLException;

    /**
     * 새로운 과목 데이터를 데이터베이스에 저장합니다.
     *
     * @param subjectDTO 저장할 {@link SubjectDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 동안 오류가 발생한 경우
     */
    void save(SubjectDTO subjectDTO) throws SQLException;

    /**
     * 기존의 과목 데이터를 업데이트합니다.
     *
     * @param subjectDTO 업데이트할 {@link SubjectDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void update(SubjectDTO subjectDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 과목 데이터를 삭제합니다.
     *
     * @param id 삭제할 과목 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 동안 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}