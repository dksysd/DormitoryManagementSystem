package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionApplicationStatus implements Model {
    private Integer id;
    private String statusName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionApplicationStatusDTO.builder()
                .id(id)
                .statusName(statusName)
                .description(description)
                .build();
    }
}
