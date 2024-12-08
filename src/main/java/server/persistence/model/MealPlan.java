package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.DormitoryDTO;
import server.persistence.dto.MealPlanDTO;
import server.persistence.dto.MealPlanTypeDTO;

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
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
