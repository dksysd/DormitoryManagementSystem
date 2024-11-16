package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.GradeLevel;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeLevelDTO implements DTO {
    private Integer id;
    private String levelName;
    private Float scaledScore;

    @Override
    public Model toModel() {
        return (Model) GradeLevel.builder()
                .id(id)
                .levelName(levelName)
                .scaledScore(scaledScore);
    }
}
