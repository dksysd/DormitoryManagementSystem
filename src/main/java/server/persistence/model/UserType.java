package server.persistence.model;

import lombok.*;
import server.persistence.dto.DTO;
import server.persistence.dto.UserTypeDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserType implements Model {
    private Integer id;
    private String typeName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) UserTypeDTO.builder()
                .id(id)
                .typeName(typeName)
                .description(description)
                .build();
    }
}
