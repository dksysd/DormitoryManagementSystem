package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.DormitoryRoomTypeDTO;
import server.persistence.dto.SelectionQuotaDTO;
import server.persistence.dto.SelectionScheduleDTO;

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
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO)
                .build();
    }
}
