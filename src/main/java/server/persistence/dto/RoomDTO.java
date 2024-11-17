package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Room;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO implements DTO {
    private Integer id;
    private String roomNumber;

    private DormitoryRoomTypeDTO dormitoryRoomTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public Model toModel() {
        return (Model) Room.builder()
                .id(id)
                .roomNumber(roomNumber)
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}