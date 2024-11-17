package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.MealPlanType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanTypeDTO implements DTO {
    private Integer id;
    private String typeName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) MealPlanType.builder()
                .id(id)
                .typeName(typeName)
                .description(description)
                .build();
    }
}
