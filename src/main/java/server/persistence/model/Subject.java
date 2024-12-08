package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.SubjectDTO;
import server.persistence.dto.UserDTO;

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
        return (DTO) SubjectDTO.builder()
                .id(id)
                .subject_name(subject_name)
                .description(description)
                .credit(credit)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .professorId(professorId)
                .build();
    }
}
