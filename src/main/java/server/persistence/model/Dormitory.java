package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dormitory implements Model {
    private Integer id;
    private String name;
    private String description;

    @Override
    public DTO toDTO() {
        return (DTO) DormitoryDTO.builder()
                .id(id)
                .name(name)
                .description(description);
    }
}
