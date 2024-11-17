package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveOutRequestStatus implements Model {
    private Integer id;
    private String statusName;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) MoveOutRequestStatusDTO.builder()
                .id(id)
                .statusName(statusName)
                .description(description)
                .build();
    }
}
