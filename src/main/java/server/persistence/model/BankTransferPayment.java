package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferPayment implements Model {
    private Integer id;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime createdAt;

    private PaymentDTO paymentDTO;
    private BankDTO bankDTO;

    @Override
    public DTO toDTO() {
        return (DTO) BankTransferPaymentDTO.builder()
                .id(id)
                .accountNumber(accountNumber)
                .accountHolderName(accountHolderName)
                .createdAt(createdAt)
                .paymentDTO(paymentDTO)
                .bankDTO(bankDTO);
    }
}
