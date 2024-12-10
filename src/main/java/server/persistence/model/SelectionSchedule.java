package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.SelectionScheduleDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionSchedule implements Model {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime statedAt;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionScheduleDTO.builder()
                .id(id)
                .title(title)
                .createdAt(createdAt)
                .startedAt(statedAt)
                .build();
    }
}
