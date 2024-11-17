package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.PaymentHistory;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryDTO implements DTO {
    private Integer id;

    private UserDTO userDTO;
    private PaymentDTO paymentDTO;

    @Override
    public Model toModel() {
        return (Model) PaymentHistory.builder()
                .id(id)
                .userDTO(userDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
