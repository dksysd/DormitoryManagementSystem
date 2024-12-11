package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.CardIssuerDTO;
import server.persistence.dto.DTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardIssuer implements Model {
    private Integer id;
    private String issuerName;

    @Override
    public DTO toDTO() {
        return (DTO) CardIssuerDTO.builder()
                .id(id)
                .issuerName(issuerName)
                .build();
    }
}
