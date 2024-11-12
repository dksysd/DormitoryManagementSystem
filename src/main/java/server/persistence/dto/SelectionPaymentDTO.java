package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionPayment;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionPaymentDTO implements DTO {
    private Integer id;

    private SelectionDTO selectionDTO;
    private PaymentDTO paymentDTO;

    @Override
    public Model toModel() {
        return (Model) SelectionPayment.builder()
                .id(id)
                .selectionDTO(selectionDTO)
                .paymentDTO(paymentDTO);
    }
}
