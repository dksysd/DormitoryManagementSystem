package shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Getter
@Setter
@Builder
public class UserTypeDto {
    private int id;
    private String type;
}
