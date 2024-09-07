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
public class SubjectDto {
    private int id;
    private String name;
}
