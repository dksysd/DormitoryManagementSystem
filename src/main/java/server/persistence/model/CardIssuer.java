package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardIssuer implements Model {
    private Integer id;
    private String issuerName;
    private String issuerCode;

    @Override
    public DTO toDTO() {
        return (DTO) CardIssuerDTO.builder()
                .id(id)
                .issuerName(issuerName)
                .issuerCode(issuerCode)
                .build();
    }
}
