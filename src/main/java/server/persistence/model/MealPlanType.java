package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.MealPlanTypeDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanType implements Model {
    private Integer id;
    private String typeName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) MealPlanTypeDTO.builder()
                .id(id)
                .typeName(typeName)
                .description(description)
                .build();
    }
}
