package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomAssignment implements Model {
    private Integer id;
    private Integer bedNumber;
    private LocalDateTime moveInAt;
    private LocalDateTime moveOutAt;
    private LocalDateTime createdAt;

    private SelectionDTO selectionDTO;
    private RoomDTO roomDTO;

    @Override
    public DTO toDTO() {
        return (DTO) RoomAssignmentDTO.builder()
                .id(id)
                .bedNumber(bedNumber)
                .moveInAt(moveInAt)
                .moveOutAt(moveOutAt)
                .createdAt(createdAt)
                .selectionDTO(selectionDTO)
                .roomDTO(roomDTO);
    }
}
