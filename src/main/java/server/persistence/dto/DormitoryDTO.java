package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Dormitory;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryDTO implements DTO {
    private Integer id;
    private String name;
    private String description;

    @Override
    public Model toModel() {
        return (Model) Dormitory.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}
