package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Payment;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO implements DTO {
    private Integer id;
    private Integer paymentAmount;
    private LocalDateTime createdAt;

    private PaymentCodeDTO paymentCodeDTO;
    private PaymentStatusDTO paymentStatusDTO;
    private PaymentMethodDTO paymentMethodDTO;

    @Override
    public Model toModel() {
        return (Model) Payment.builder()
                .id(id)
                .paymentAmount(paymentAmount)
                .createdAt(createdAt)
                .paymentCodeDTO(paymentCodeDTO)
                .paymentStatusDTO(paymentStatusDTO)
                .paymentMethodDTO(paymentMethodDTO);
    }
}
