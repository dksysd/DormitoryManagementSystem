package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.MoveOutRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveOutRequestDTO implements DTO {
    private Integer id;
    private LocalDateTime checkoutAt;
    private String accountNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MoveOutRequestStatusDTO moveOutRequestStatusDTO;
    private SelectionDTO selectionDTO;
    private BankDTO bankDTO;

    @Override
    public Model toModel() {
        return (Model) MoveOutRequest.builder()
                .id(id)
                .checkoutAt(checkoutAt)
                .accountNumber(accountNumber)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .moveOutRequestStatusDTO(moveOutRequestStatusDTO)
                .selectionDTO(selectionDTO)
                .bankDTO(bankDTO);
    }
}
