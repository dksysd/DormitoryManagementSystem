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
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SubjectDTO subjectDTO;
    private UserDTO userDTO;
    private GradeLevelDTO gradeLevelDTO;

    public DTO toDTO() {
        return (DTO) GradeDTO.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .subjectDTO(subjectDTO)
                .userDTO(userDTO)
                .gradeLevelDTO(gradeLevelDTO);
    }
}
