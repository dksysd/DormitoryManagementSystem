package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionQuota implements Model {
    private Integer id;
    private Integer quota;

    private SelectionScheduleDTO selectionScheduleDTO;
    private DormitoryRoomTypeDTO dormitoryRoomTypeDTO;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionQuotaDTO.builder()
                .id(id)
                .quota(quota)
                .selectionScheduleDTO(selectionScheduleDTO)
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO);
    }
}
