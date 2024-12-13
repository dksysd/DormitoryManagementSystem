package server.persistence.dao;

import server.persistence.dto.DormitoryRoomTypeDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * DormitoryRoomTypeDAOI 인터페이스는 데이터베이스에서 기숙사 방 타입 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 기숙사 방 타입 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface DormitoryRoomTypeDAOI {

    /**
     * 주어진 ID를 기반으로 기숙사 방 타입 데이터를 검색합니다.
     *
     * @param id 검색할 기숙사 방 타입 데이터의 고유 ID
     * @return ID에 해당하는 {@link DormitoryRoomTypeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    DormitoryRoomTypeDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 UID를 기반으로 기숙사 방 타입 데이터를 검색합니다.
     *
     * @param uid 검색할 기숙사 방 타입 데이터의 고유 UID
     * @return UID에 해당하는 {@link DormitoryRoomTypeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    DormitoryRoomTypeDTO findByUid(String uid) throws SQLException;

    /**
     * 모든 기숙사 방 타입 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 기숙사 방 타입 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<DormitoryRoomTypeDTO> findAll() throws SQLException;

    /**
     * 새로운 기숙사 방 타입 데이터를 데이터베이스에 저장합니다.
     *
     * @param dormitoryRoomTypeDTO 저장할 {@link DormitoryRoomTypeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException;

    /**
     * 기존 기숙사 방 타입 데이터를 업데이트합니다.
     *
     * @param dormitoryRoomTypeDTO 업데이트할 {@link DormitoryRoomTypeDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(DormitoryRoomTypeDTO dormitoryRoomTypeDTO) throws SQLException;

    /**
     * 주어진 UID와 기숙사 이름을 기반으로 기숙사 데이터를 업데이트합니다.
     *
     * @param uid           업데이트할 데이터의 고유 UID
     * @param dormitoryName 기숙사 이름
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void updateDormitory(String uid, String dormitoryName) throws SQLException;

    /**
     * 주어진 ID에 해당하는 기숙사 방 타입 데이터를 삭제합니다.
     *
     * @param id 삭제할 기숙사 방 타입 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}