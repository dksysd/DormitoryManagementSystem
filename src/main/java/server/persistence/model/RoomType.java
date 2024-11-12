package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomType implements Model {
    private Integer id;
    private String typeName;
    private String description;
    private Integer maxPerson;

    @Override
    public DTO toDTO() {
        return (DTO) RoomTypeDTO.builder()
                .id(id)
                .typeName(typeName)
                .description(description)
                .maxPerson(maxPerson);
    }
}
