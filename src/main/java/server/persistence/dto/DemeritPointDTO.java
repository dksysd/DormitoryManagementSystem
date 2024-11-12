package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.DemeritPoint;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemeritPointDTO implements DTO {
    private Integer id;
    private String description;
    private LocalDateTime createdAt;

    private UserDTO userDTO;
    private RoomAssignmentDTO roomAssignmentDTO;

    @Override
    public Model toModel() {
        return (Model) DemeritPoint.builder()
                .id(id)
                .description(description)
                .createdAt(createdAt)
                .userDTO(userDTO)
                .roomAssignmentDTO(roomAssignmentDTO);
    }
}
