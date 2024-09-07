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
public class RoomAssignmentDto {
    private int id;
    private SelectionScheduleDto selectionScheduleDto;
    private UserDto studentDto;
    private RoomDto roomDto;
    private int bedNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String tuberculosisCertificatePath;
    private boolean isPaid;
    private boolean isMoveOut;
}
