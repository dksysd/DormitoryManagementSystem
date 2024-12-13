package server.persistence.dao;

import server.persistence.dto.UserTypeDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * UserTypeDAOI 인터페이스는 데이터베이스에서 사용자 유형 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 사용자 유형 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface UserTypeDAOI {

    /**
     * 주어진 ID를 기반으로 사용자 유형 데이터를 검색합니다.
     *
     * @param id 검색할 사용자 유형 데이터의 고유 ID
     * @return 주어진 ID에 해당하는 {@link UserTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 과정에서 오류가 발생한 경우
     */
    UserTypeDTO findById(Integer id) throws SQLException;

    /**
     * 사용자 유형 이름을 기준으로 사용자 유형 데이터를 검색합니다.
     *
     * @param typeName 검색할 사용자 유형의 이름
     * @return 주어진 이름에 해당하는 {@link UserTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 과정에서 오류가 발생한 경우
     */
    UserTypeDTO findByTypeName(String typeName) throws SQLException;

    /**
     * 모든 사용자 유형 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 사용자 유형 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 과정에서 오류가 발생한 경우
     */
    List<UserTypeDTO> findAll() throws SQLException;

    /**
     * 새로운 사용자 유형 데이터를 데이터베이스에 저장합니다.
     *
     * @param userTypeDTO 저장할 {@link UserTypeDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 과정에서 오류가 발생한 경우
     */
    void save(UserTypeDTO userTypeDTO) throws SQLException;

    /**
     * 기존의 사용자 유형 데이터를 업데이트합니다.
     *
     * @param userTypeDTO 업데이트할 {@link UserTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 과정에서 오류가 발생한 경우
     */
    void update(UserTypeDTO userTypeDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 사용자 유형 데이터를 삭제합니다.
     *
     * @param id 삭제할 사용자 유형 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 과정에서 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}