package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionPayment implements Model {
    private Integer id;

    private SelectionDTO selectionDTO;
    private PaymentDTO paymentDTO;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionPaymentDTO.builder()
                .id(id)
                .selectionDTO(selectionDTO)
                .paymentDTO(paymentDTO);
    }
}
