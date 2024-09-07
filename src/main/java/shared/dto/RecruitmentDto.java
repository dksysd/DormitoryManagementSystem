package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecruitmentDto {
    private SelectionScheduleDto selectionScheduleDto;
    private RoomTypeDto roomTypeDto;
    private int maxPerson;
}
