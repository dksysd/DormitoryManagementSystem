package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPayment implements Model {
    private Integer id;
    private String cardNumber;
    private LocalDateTime createdAt;

    private CardIssuerDTO cardIssuerDTO;
    private PaymentDTO paymentDTO;

    @Override
    public DTO toDTO() {
        return (DTO) CardPaymentDTO.builder()
                .id(id)
                .cardNumber(cardNumber)
                .createdAt(createdAt)
                .cardIssuerDTO(cardIssuerDTO)
                .paymentDTO(paymentDTO);
    }
}
