package server.persistence.dao;

import server.persistence.dto.CardIssuerDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * CardIssuerDAOI 인터페이스는 데이터베이스에서 카드 발급기관 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 카드 발급기관 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface CardIssuerDAOI {

    /**
     * 주어진 ID를 기반으로 카드 발급기관 데이터를 검색합니다.
     *
     * @param id 검색할 카드 발급기관 데이터의 고유 ID
     * @return ID에 해당하는 {@link CardIssuerDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    CardIssuerDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 이름을 기반으로 카드 발급기관 데이터를 검색합니다.
     *
     * @param issuerName 검색할 카드 발급기관 이름
     * @return 이름에 해당하는 {@link CardIssuerDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    CardIssuerDTO findByName(String issuerName) throws SQLException;

    /**
     * 주어진 코드를 기반으로 카드 발급기관 데이터를 검색합니다.
     *
     * @param issuerCode 검색할 카드 발급기관 코드
     * @return 코드에 해당하는 {@link CardIssuerDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    CardIssuerDTO findByCode(String issuerCode) throws SQLException;

    /**
     * 모든 카드 발급기관 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 있는 모든 카드 발급기관 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<CardIssuerDTO> findAll() throws SQLException;

    /**
     * 새로운 카드 발급기관 데이터를 데이터베이스에 저장합니다.
     *
     * @param cardIssuerDTO 저장할 {@link CardIssuerDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(CardIssuerDTO cardIssuerDTO) throws SQLException;

    /**
     * 기존의 카드 발급기관 데이터를 업데이트합니다.
     *
     * @param cardIssuerDTO 업데이트할 {@link CardIssuerDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(CardIssuerDTO cardIssuerDTO) throws SQLException;

    /**
     * 주어진 ID에 해당하는 카드 발급기관 데이터를 삭제합니다.
     *
     * @param id 삭제할 카드 발급기관 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}