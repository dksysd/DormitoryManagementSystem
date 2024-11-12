package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.PaymentCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCodeDTO implements DTO {
    private Integer id;
    private String paymentCode;
    private String description;

    @Override
    public Model toModel() {
        return (Model) PaymentCode.builder()
                .id(id)
                .paymentCode(paymentCode)
                .description(description);
    }
}
