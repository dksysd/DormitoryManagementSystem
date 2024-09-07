package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RefundDetailDto {
    private DormitoryMoveOutApplyDto dormitoryMoveOutApply;
    private int amount;
    private LocalDate date;
}
