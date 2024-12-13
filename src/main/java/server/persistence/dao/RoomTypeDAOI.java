package server.persistence.dao;

import server.persistence.dto.RoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * RoomTypeDAOI 인터페이스는 데이터베이스에서 방 유형 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 방 유형 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface RoomTypeDAOI {

    /**
     * 주어진 ID를 기반으로 방 유형 데이터를 검색합니다.
     *
     * @param id 검색할 방 유형 데이터의 고유 ID
     * @return 주어진 ID에 해당하는 {@link RoomTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    RoomTypeDTO findById(Integer id) throws SQLException;

    /**
     * 특정 이름을 기반으로 방 유형 데이터를 검색합니다.
     *
     * @param name 검색할 방 유형의 이름
     * @return 해당 이름에 해당하는 {@link RoomTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    RoomTypeDTO findByName(String name) throws SQLException;

    /**
     * 모든 방 유형 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 방 유형 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<RoomTypeDTO> findAll() throws SQLException;

    /**
     * 모든 방 유형 데이터를 문자열 형식으로 검색하여 반환합니다.
     * <p>
     * 각 항목은 방 유형 이름과 최대 인원수를 포함하는 문자열로 구성됩니다.
     *
     * @return 데이터베이스에 저장된 방 유형 데이터를 문자열 형태로 변환한 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<String> findAllIntoString() throws SQLException;

    /**
     * 새로운 방 유형 데이터를 데이터베이스에 저장합니다.
     *
     * @param roomTypeDTO 저장할 {@link RoomTypeDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 동안 오류가 발생한 경우
     */
    void save(RoomTypeDTO roomTypeDTO) throws SQLException;

    /**
     * 기존 방 유형 데이터를 업데이트합니다.
     *
     * @param roomTypeDTO 업데이트할 {@link RoomTypeDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void update(RoomTypeDTO roomTypeDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 방 유형 데이터를 삭제합니다.
     *
     * @param id 삭제할 방 유형 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 동안 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}