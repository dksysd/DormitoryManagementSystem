package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryMoveInApplyDto {
    private SelectionScheduleDto selectionScheduleDto;
    private UserDto studentDto;
    private RoomTypeDto roomTypeDto;
    private boolean isApproval;
}
