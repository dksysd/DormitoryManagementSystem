package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.RoomType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDTO implements DTO {
    private Integer id;
    private String typeName;
    private String description;
    private Integer maxPerson;

    @Override
    public Model toModel() {
        return (Model) RoomType.builder()
                .id(id)
                .typeName(typeName)
                .description(description)
                .maxPerson(maxPerson);
    }
}
