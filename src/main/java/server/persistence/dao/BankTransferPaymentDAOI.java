package server.persistence.dao;

import server.persistence.dto.BankTransferPaymentDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * BankTransferPaymentDAOI 인터페이스는 데이터베이스에서 계좌 이체 결제 데이터를 관리하기 위한 메서드를 정의합니다.
 * <p>
 * 이 인터페이스는 계좌 이체 데이터를 검색, 저장, 업데이트 및 삭제하는 기능을 제공합니다.
 */
public interface BankTransferPaymentDAOI {

    /**
     * 주어진 ID를 기반으로 계좌 이체 결제 데이터를 검색합니다.
     *
     * @param id 검색할 계좌 이체 결제 데이터의 고유 ID
     * @return ID에 해당하는 {@link BankTransferPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    BankTransferPaymentDTO findById(Integer id) throws SQLException;

    /**
     * 모든 계좌 이체 결제 데이터를 검색하여 반환합니다.
     *
     * @return 데이터베이스에 있는 모든 계좌 이체 결제 데이터의 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    List<BankTransferPaymentDTO> findAll() throws SQLException;

    /**
     * 새로운 계좌 이체 결제 데이터를 데이터베이스에 저장합니다.
     *
     * @param bankTransferPaymentDTO 저장할 {@link BankTransferPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void save(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;

    /**
     * 기존 계좌 이체 결제 데이터를 업데이트합니다.
     *
     * @param bankTransferPaymentDTO 업데이트할 {@link BankTransferPaymentDTO} 객체
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(BankTransferPaymentDTO bankTransferPaymentDTO) throws SQLException;

    /**
     * 계좌 이체 결제 데이터를 주어진 UID 및 상세 정보로 업데이트합니다.
     *
     * @param uid               업데이트할 데이터의 고유 식별자
     * @param accountNumber     계좌 번호
     * @param accountHolderName 계좌 소유자 이름
     * @param bankName          은행 이름
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void update(String uid, String accountNumber, String accountHolderName, String bankName) throws SQLException;

    /**
     * 주어진 ID에 해당하는 계좌 이체 결제 데이터를 삭제합니다.
     *
     * @param id 삭제할 계좌 이체 결제 데이터의 고유 ID
     * @throws SQLException 데이터베이스 접근 중 오류가 발생한 경우
     */
    void delete(Integer id) throws SQLException;
}