package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.DemeritPointDTO;
import server.persistence.dto.RoomAssignmentDTO;
import server.persistence.dto.UserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemeritPoint implements Model {
    private Integer id;
    private String description;
    private LocalDateTime createdAt;

    private UserDTO userDTO;
    private RoomAssignmentDTO roomAssignmentDTO;

    @Override
    public DTO toDTO() {
        return (DTO) DemeritPointDTO.builder()
                .id(id)
                .description(description)
                .createdAt(createdAt)
                .userDTO(userDTO)
                .roomAssignmentDTO(roomAssignmentDTO)
                .build();
    }
}
