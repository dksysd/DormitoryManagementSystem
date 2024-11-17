package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.BankTransferPayment;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferPaymentDTO implements DTO {
    private Integer id;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime createdAt;

    private PaymentDTO paymentDTO;
    private BankDTO bankDTO;

    @Override
    public Model toModel() {
        return (Model) BankTransferPayment.builder()
                .id(id)
                .accountNumber(accountNumber)
                .accountHolderName(accountHolderName)
                .createdAt(createdAt)
                .paymentDTO(paymentDTO)
                .bankDTO(bankDTO)
                .build();
    }
}
