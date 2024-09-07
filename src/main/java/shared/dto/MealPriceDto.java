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
public class MealPriceDto {
    private int id;
    private DormitoryDto dormitoryDto;
    private int price;
    private String type;
}
