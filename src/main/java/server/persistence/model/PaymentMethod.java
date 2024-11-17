package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod implements Model {
    private Integer id;
    private String methodName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) PaymentMethodDTO.builder()
                .id(id)
                .methodName(methodName)
                .description(description)
                .build();
    }
}
