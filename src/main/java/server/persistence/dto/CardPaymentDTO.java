package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.CardPayment;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentDTO implements DTO {
    private Integer id;
    private String cardNumber;
    private LocalDateTime createdAt;

    private CardIssuerDTO cardIssuerDTO;
    private PaymentDTO paymentDTO;

    @Override
    public Model toModel() {
        return (Model) CardPayment.builder()
                .id(id)
                .cardNumber(cardNumber)
                .createdAt(createdAt)
                .cardIssuerDTO(cardIssuerDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
