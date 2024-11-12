package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionApplication;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionApplicationDTO implements DTO {
    private Integer id;
    private Integer preference;
    private boolean hasSleepHabit;
    private boolean isYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SelectionApplicationStatusDTO selectionApplicationStatusDTO;
    private SelectionScheduleDTO selectionScheduleDTO;
    private DormitoryRoomTypeDTO dormitoryRoomTypeDTO;
    private MealPlanDTO mealPlanDTO;
    private UserDTO roommateUserDTO;
    private UserDTO userDTO;

    @Override
    public Model toModel() {
        return (Model) SelectionApplication.builder()
                .id(id)
                .preference(preference)
                .hasSleepHabit(hasSleepHabit)
                .isYear(isYear)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionApplicationStatusDTO(selectionApplicationStatusDTO)
                .selectionScheduleDTO(selectionScheduleDTO)
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO)
                .mealPlanDTO(mealPlanDTO)
                .roommateUserDTO(roommateUserDTO)
                .userDTO(userDTO);
    }
}
