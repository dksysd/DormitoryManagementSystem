package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveOutRequest implements Model {
    private Integer id;
    private LocalDateTime checkoutAt;
    private String accountNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MoveOutRequestStatusDTO moveOutRequestStatusDTO;
    private SelectionDTO selectionDTO;
    private BankDTO bankDTO;

    @Override
    public DTO toDTO() {
        return (DTO) MoveOutRequestDTO.builder()
                .id(id)
                .checkoutAt(checkoutAt)
                .accountNumber(accountNumber)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .moveOutRequestStatusDTO(moveOutRequestStatusDTO)
                .selectionDTO(selectionDTO)
                .bankDTO(bankDTO)
                .build();
    }
}
