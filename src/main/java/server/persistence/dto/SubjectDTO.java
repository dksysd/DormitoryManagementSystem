package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Subject;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SubjectDTO implements DTO {
    private Integer id;
    private String subject_name;
    private String description;
    private Integer credit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDTO professorId;

    @Override
    public Model toModel() {
        return new Subject(id,subject_name,description,credit,createdAt,updatedAt,professorId);
    }
}
