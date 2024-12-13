package server.persistence.model;

import server.persistence.dto.DTO;

/**
 * Model 인터페이스는 데이터 모델이 {@link DTO} 객체로 변환될 수 있도록 정의합니다.
 * <p>
 * 이 인터페이스를 구현하는 클래스는 데이터를 전송 객체(DTO)로 변환하기 위해 {@code toDTO()} 메서드를 구현해야 합니다.
 */
public interface Model {

    /**
     * 데이터를 전송 객체(DTO)로 변환합니다.
     *
     * @return 데이터 모델에 해당하는 {@link DTO} 객체
     */
    DTO toDTO();
}