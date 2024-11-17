package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRefund implements Model {
    private Integer id;
    private String refundReason;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime createdAt;

    private BankDTO bankDTO;
    private PaymentDTO paymentDTO;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentRefundDTO.builder()
                .id(id)
                .refundReason(refundReason)
                .accountNumber(accountNumber)
                .accountHolderName(accountHolderName)
                .createdAt(createdAt)
                .bankDTO(bankDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
