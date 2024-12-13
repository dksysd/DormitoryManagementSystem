package server.persistence.dao;

import server.persistence.dto.CardPaymentDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * CardPaymentDAOI 인터페이스는 데이터베이스에서 카드 결제 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 카드 결제 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface CardPaymentDAOI {

    /**
     * 주어진 ID를 기반으로 카드 결제 데이터를 검색합니다.
     *
     * @param id 검색할 카드 결제 데이터의 고유 ID
     * @return ID에 해당하는 {@link CardPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    CardPaymentDTO findById(Integer id) throws SQLException;

    /**
     * 주어진 카드 번호를 기반으로 카드 결제 데이터를 검색합니다.
     *
     * @param cardNumber 검색할 카드 결제 데이터의 카드 번호
     * @return 카드 번호에 해당하는 {@link CardPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    CardPaymentDTO findByCardNumber(String cardNumber) throws SQLException;

    /**
     * 모든 카드 결제 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 저장된 모든 카드 결제 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<CardPaymentDTO> findAll() throws SQLException;

    /**
     * 새로운 카드 결제 데이터를 데이터베이스에 저장합니다.
     *
     * @param cardPaymentDTO 저장할 {@link CardPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(CardPaymentDTO cardPaymentDTO) throws SQLException;

    /**
     * 기존 카드 결제 데이터를 업데이트합니다.
     *
     * @param cardPaymentDTO 업데이트할 {@link CardPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(CardPaymentDTO cardPaymentDTO) throws SQLException;

    /**
     * UID 및 기타 카드 결제 정보를 기반으로 결제 데이터를 업데이트합니다.
     *
     * @param uid            업데이트할 카드 결제 데이터의 식별자
     * @param cardNumber     카드 번호
     * @param cardIssuerName 카드 발급기관 이름
     * @param paymentStatus  결제 상태
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(String uid, String cardNumber, String cardIssuerName, String paymentStatus) throws SQLException;

    /**
     * 주어진 ID에 해당하는 카드 결제 데이터를 삭제합니다.
     *
     * @param id 삭제할 카드 결제 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}