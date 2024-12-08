package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.SelectionPhaseDTO;
import server.persistence.dto.SelectionScheduleDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionPhase implements Model {
    private Integer id;
    private String phaseName;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SelectionScheduleDTO selectionScheduleDTO;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionPhaseDTO.builder()
                .id(id)
                .phaseName(phaseName)
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionScheduleDTO(selectionScheduleDTO)
                .build();
    }
}
