package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealPlan implements Model {
    private Integer id;
    private Integer price;

    private MealPlanTypeDTO mealPlanTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public DTO toDTO() {
        return (DTO) MealPlanDTO.builder()
                .id(id)
                .price(price)
                .mealPlanTypeDTO(mealPlanTypeDTO)
                .dormitoryDTO(dormitoryDTO);
    }
}
