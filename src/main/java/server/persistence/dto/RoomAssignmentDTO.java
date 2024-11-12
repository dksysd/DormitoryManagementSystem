package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.RoomAssignment;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAssignmentDTO implements DTO {
    private Integer id;
    private Integer bedNumber;
    private LocalDateTime moveInAt;
    private LocalDateTime moveOutAt;
    private LocalDateTime createdAt;

    private SelectionDTO selectionDTO;
    private RoomDTO roomDTO;

    @Override
    public Model toModel() {
        return (Model) RoomAssignment.builder()
                .id(id)
                .bedNumber(bedNumber)
                .moveInAt(moveInAt)
                .moveOutAt(moveOutAt)
                .createdAt(createdAt)
                .selectionDTO(selectionDTO)
                .roomDTO(roomDTO);
    }
}
