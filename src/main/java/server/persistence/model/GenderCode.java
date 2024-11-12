package server.persistence.model;

import lombok.*;
import server.persistence.dto.DTO;
import server.persistence.dto.GenderCodeDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenderCode implements Model{
    private Integer id;
    private String codeName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) GenderCodeDTO.builder()
                .id(id)
                .codeName(codeName)
                .description(description);
    }
}
