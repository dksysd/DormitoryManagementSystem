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
public class UserDto {
    private int id;
    private String name;
    private AddressDto addressDto;
    private UserTypeDto userTypeDto;
    private LoginInfoDto loginInfoDto;
}
