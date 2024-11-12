package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Grade;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeDTO implements DTO{
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SubjectDTO subjectDTO;
    private UserDTO userDTO;
    private GradeLevelDTO gradeLevelDTO;

    public Model toModel() {
        return (Model) Grade.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .subjectDTO(subjectDTO)
                .userDTO(userDTO)
                .gradeLevelDTO(gradeLevelDTO);
    }
}
