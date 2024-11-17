package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Model {
    private Integer id;
    private Integer paymentAmount;
    private LocalDateTime createdAt;

    private PaymentCodeDTO paymentCodeDTO;
    private PaymentStatusDTO paymentStatusDTO;
    private PaymentMethodDTO paymentMethodDTO;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentDTO.builder()
                .id(id)
                .paymentAmount(paymentAmount)
                .createdAt(createdAt)
                .paymentCodeDTO(paymentCodeDTO)
                .paymentStatusDTO(paymentStatusDTO)
                .paymentMethodDTO(paymentMethodDTO)
                .build();
    }
}
