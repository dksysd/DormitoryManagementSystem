package server.persistence.dto;

import lombok.*;
import server.persistence.model.GenderCode;
import server.persistence.model.Model;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenderCodeDTO implements DTO {
    private Integer id;
    private String codeName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) GenderCode.builder()
                .id(id)
                .codeName(codeName)
                .description(description);
    }
}
