package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.SelectionQuota;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionQuotaDTO implements DTO {
    private Integer id;
    private Integer quota;

    private SelectionScheduleDTO selectionScheduleDTO;
    private DormitoryRoomTypeDTO dormitoryRoomTypeDTO;

    @Override
    public Model toModel() {
        return (Model) SelectionQuota.builder()
                .id(id)
                .quota(quota)
                .selectionScheduleDTO(selectionScheduleDTO)
                .dormitoryRoomTypeDTO(dormitoryRoomTypeDTO);
    }
}
