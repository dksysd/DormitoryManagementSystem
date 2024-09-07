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
public class DormitoryMoveOutApplyDto {
    private int id;
    private RoomAssignmentDto roomAssignmentDto;
    private LocalDate date;
    private String bank;
    private String accountNumber;
}
