package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.GradeLevelDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeLevel implements Model {
    private Integer id;
    private String levelName;
    private Float scaledScore;

    @Override
    public DTO toDTO() {
        return (DTO) GradeLevelDTO.builder()
                .id(id)
                .levelName(levelName)
                .scaledScore(scaledScore)
                .build();
    }
}
