package server.persistence.dto;


import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.UserType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeDTO implements DTO {
    private Integer id;
    private String typeName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) UserType.builder()
                .id(id)
                .typeName(typeName)
                .description(description);
    }
}
