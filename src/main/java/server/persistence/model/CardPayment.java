package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.CardIssuerDTO;
import server.persistence.dto.CardPaymentDTO;
import server.persistence.dto.DTO;
import server.persistence.dto.PaymentDTO;

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
                .paymentDTO(paymentDTO)
                .build();
    }
}
