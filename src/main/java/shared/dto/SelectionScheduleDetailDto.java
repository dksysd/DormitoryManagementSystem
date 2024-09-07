package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionScheduleDetailDto {
    private SelectionScheduleDto selectionScheduleDto;
    private String schedule;
    private LocalDateTime beginDate;
    private int durationHour;
}
