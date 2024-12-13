package server.persistence.dao;

import server.persistence.dto.SelectionApplicationDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * SelectionApplicationDAOI 인터페이스는 데이터베이스에서 선택 신청 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 선택 신청 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface SelectionApplicationDAOI {

    /**
     * 주어진 ID를 기반으로 선택 신청 데이터를 검색합니다.
     *
     * @param id 검색할 선택 신청 데이터의 고유 ID
     * @return 주어진 ID에 해당하는 {@link SelectionApplicationDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    SelectionApplicationDTO findById(Integer id) throws SQLException;

    /**
     * 특정 사용자의 UID를 기반으로 선택 신청 데이터를 검색합니다.
     *
     * @param uid 검색할 사용자의 UID
     * @return UID에 해당하는 {@link SelectionApplicationDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    SelectionApplicationDTO findByUid(String uid) throws SQLException;

    /**
     * 모든 선택 신청 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 선택 신청 데이터의 리스트
     * @throws SQLException 데이터베이스에서 데이터를 검색하는 동안 오류가 발생한 경우
     */
    List<SelectionApplicationDTO> findAll() throws SQLException;

    /**
     * 새로운 선택 신청 데이터를 데이터베이스에 저장합니다.
     *
     * @param selectionApplicationDTO 저장할 {@link SelectionApplicationDTO} 객체
     * @throws SQLException 데이터베이스에 데이터를 삽입하는 동안 오류가 발생한 경우
     */
    void save(SelectionApplicationDTO selectionApplicationDTO) throws SQLException;

    /**
     * 기존의 선택 신청 데이터를 업데이트합니다.
     *
     * @param selectionApplicationDTO 업데이트할 {@link SelectionApplicationDTO} 객체
     * @throws SQLException 데이터베이스에서 데이터를 수정하는 동안 오류가 발생한 경우
     */
    void update(SelectionApplicationDTO selectionApplicationDTO) throws SQLException;

    /**
     * 주어진 UID를 사용하여 사용자의 선택 신청에서 식사 계획을 업데이트합니다.
     *
     * @param uid          사용자 고유 식별자
     * @param mealPlanName 새로 설정할 식사 계획의 이름
     * @throws SQLException 데이터베이스에서 데이터를 검색하거나 수정하는 동안 오류가 발생한 경우
     */
    void updateMealPlan(String uid, String mealPlanName) throws SQLException;

    /**
     * 주어진 UID를 사용하여 사용자의 선택 신청 우선도를 업데이트합니다.
     *
     * @param uid        사용자 고유 식별자
     * @param preference 새로 설정할 선택 신청 우선도
     * @throws SQLException 데이터베이스에서 데이터를 검색하거나 수정하는 동안 오류가 발생한 경우
     */
    void updatePreference(String uid, Integer preference) throws SQLException;

    /**
     * 주어진 UID를 사용하여 사용자의 선택 신청에서 기숙사 방 유형을 업데이트합니다.
     *
     * @param uid          사용자 고유 식별자
     * @param roomTypeName 새로 설정할 방 유형의 이름
     * @throws SQLException 데이터베이스에서 데이터를 검색하거나 수정하는 동안 오류가 발생한 경우
     */
    void updateRoomType(String uid, String roomTypeName) throws SQLException;

    /**
     * 주어진 UID를 사용하여 선택 신청 상태를 업데이트합니다.
     *
     * @param uid        사용자 고유 식별자
     * @param statusName 새로 설정할 선택 신청 상태 이름
     * @throws SQLException 데이터베이스에서 데이터를 검색하거나 수정하는 동안 오류가 발생한 경우
     */
    void updateSelectionApplication(String uid, String statusName) throws SQLException;

    /**
     * 주어진 ID에 해당하는 선택 신청 데이터를 삭제합니다.
     *
     * @param id 삭제할 선택 신청 데이터의 고유 ID
     * @throws SQLException 데이터베이스에서 데이터를 삭제하는 동안 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}