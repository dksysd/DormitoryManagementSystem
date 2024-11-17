package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCode implements Model {
    private Integer id;
    private String paymentCode;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentCodeDTO.builder()
                .id(id)
                .paymentCode(paymentCode)
                .description(description)
                .build();
    }
}
