package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

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
                .paymentDTO(paymentDTO);
    }
}
