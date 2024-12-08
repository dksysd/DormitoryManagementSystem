package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.PaymentStatusDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatus implements Model {
    private Integer id;
    private String statusName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentStatusDTO.builder()
                .id(id)
                .statusName(statusName)
                .description(description)
                .build();
    }
}
