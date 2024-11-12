package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Grade implements Model{
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SubjectDTO subjectDTO;
    private UserDTO userDTO;
    private GradeLevelDTO gradeLevelDTO;

    public DTO toDTO() {
        return new GradeDTO(id, createdAt, updatedAt, subjectDTO, userDTO, gradeLevelDTO);
    }
}
