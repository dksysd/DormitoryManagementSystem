package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryRoomType implements Model {
    private Integer id;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private RoomTypeDTO roomTypeDTO;
    private DormitoryDTO dormitoryDTO;

    @Override
    public DTO toDTO() {
        return (DTO) DormitoryRoomTypeDTO.builder()
                .id(id)
                .price(price)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .roomTypeDTO(roomTypeDTO)
                .dormitoryDTO(dormitoryDTO)
                .build();
    }
}
