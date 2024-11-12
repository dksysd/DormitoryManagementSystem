package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

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
        return new GradeLevelDTO(id, levelName, scaledScore);
    }
}
