package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {
    private int id;
    private String name;
    private int maxPerson;
    private int price;
    private DormitoryDto dormitoryDto;
}
