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
public class LoginInfoDto {
    private int id;
    private String loginId;
    private String loginPassword;
    private String salt;
}
