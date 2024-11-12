package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionApplicationStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionApplicationStatusDTO implements DTO {
    private Integer id;
    private String statusName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) SelectionApplicationStatus.builder()
                .id(id)
                .statusName(statusName)
                .description(description);
    }
}
