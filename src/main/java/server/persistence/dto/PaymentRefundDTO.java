package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.PaymentRefund;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRefundDTO implements DTO {
    private Integer id;
    private String refundReason;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime createdAt;

    private BankDTO bankDTO;
    private PaymentDTO paymentDTO;

    @Override
    public Model toModel() {
        return (Model) PaymentRefund.builder()
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
