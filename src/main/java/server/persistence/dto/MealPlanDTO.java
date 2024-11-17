package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.MealPlan;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealPlanDTO implements DTO {
    private Integer id;
    private Integer price;

    private MealPlanTypeDTO mealPlanTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public Model toModel() {
        return (Model) MealPlan.builder()
                .id(id)
                .price(price)
                .mealPlanTypeDTO(mealPlanTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
