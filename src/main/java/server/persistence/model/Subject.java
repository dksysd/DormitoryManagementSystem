package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subject implements Model{
    private Integer id;
    private String subject_name;
    private String description;
    private Integer credit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDTO professorId; // <- 이거 교수 id 이렇게 받아도 됨?

    @Override
    public DTO toDTO() {
        return new SubjectDTO(id,subject_name,description,credit,createdAt,updatedAt,professorId);
    }
}
