package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;

//TODO : 채우기
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO implements DTO{
    private String id;
    private String password;

    @Override
    public Model toModel() {
        return null;
    }
}
