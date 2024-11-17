package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionSchedule;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionScheduleDTO implements DTO {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;

    @Override
    public Model toModel() {
        return (Model) SelectionSchedule.builder()
                .id(id)
                .title(title)
                .createdAt(createdAt)
                .build();
    }
}
