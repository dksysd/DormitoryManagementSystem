package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.MoveOutRequestStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoveOutRequestStatusDTO implements DTO {
    private Integer id;
    private String statusName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) MoveOutRequestStatus.builder()
                .id(id)
                .statusName(statusName)
                .description(description)
                .build();
    }
}
