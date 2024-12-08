package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.DormitoryDTO;
import server.persistence.dto.DormitoryRoomTypeDTO;
import server.persistence.dto.RoomDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Model {
    private Integer id;
    private String roomNumber;

    private DormitoryRoomTypeDTO dormitoryRoomTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public DTO toDTO() {
        return (DTO) RoomDTO.builder()
                .id(id)
                .roomNumber(roomNumber)
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
