package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.PaymentDTO;
import server.persistence.dto.PaymentHistoryDTO;
import server.persistence.dto.UserDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory implements Model {
    private Integer id;

    private UserDTO userDTO;
    private PaymentDTO paymentDTO;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentHistoryDTO.builder()
                .id(id)
                .userDTO(userDTO)
                .paymentDTO(paymentDTO)
                .build();
    }
}
