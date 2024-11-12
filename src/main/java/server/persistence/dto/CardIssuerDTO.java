package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.CardIssuer;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardIssuerDTO implements DTO {
    private Integer id;
    private String issuerName;
    private String issuerCode;

    @Override
    public Model toModel() {
        return (Model) CardIssuer.builder()
                .id(id)
                .issuerName(issuerName)
                .issuerCode(issuerCode);
    }
}
