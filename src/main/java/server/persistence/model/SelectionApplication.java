package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionApplication implements Model {
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
    public DTO toDTO() {
        return (DTO) SelectionApplicationDTO.builder()
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
                .userDTO(userDTO)
                .build();
    }
}
