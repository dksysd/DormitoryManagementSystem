package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionPhase;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionPhaseDTO implements DTO {
    private Integer id;
    private String phaseName;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SelectionScheduleDTO selectionScheduleDTO;

    @Override
    public Model toModel() {
        return (Model) SelectionPhase.builder()
                .id(id)
                .phaseName(phaseName)
                .description(description)
                .startAt(startAt)
                .endAt(endAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionScheduleDTO(selectionScheduleDTO);
    }
}
