package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.DormitoryDTO;

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
                .description(description)
                .build();
    }
}
