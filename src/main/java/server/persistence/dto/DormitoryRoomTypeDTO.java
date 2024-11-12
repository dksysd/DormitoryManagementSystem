package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.DormitoryRoomType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryRoomTypeDTO implements DTO {
    private Integer id;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private RoomTypeDTO roomTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public Model toModel() {
        return (Model) DormitoryRoomType.builder()
                .id(id)
                .price(price)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .roomTypeDTO(roomTypeDTO)
                .dormitoryDTO(dormitoryDTO);
    }
}
