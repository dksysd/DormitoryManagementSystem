package server.persistence.dto;

import server.persistence.model.Model;

/**
 * DTO 인터페이스는 전송 객체를 {@link Model} 객체로 변환할 수 있도록 정의합니다.
 * <p>
 * 이 인터페이스를 구현하는 클래스는 DTO 데이터를 데이터 모델로 변환하기 위해 {@code toModel()} 메서드를 구현해야 합니다.
 */
public interface DTO {

    /**
     * 전송 객체를 데이터 모델로 변환합니다.
     *
     * @return 전송 객체에 해당하는 {@link Model} 객체
     */
    Model toModel();
}