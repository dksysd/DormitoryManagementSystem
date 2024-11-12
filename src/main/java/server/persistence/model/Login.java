package server.persistence.model;

import lombok.*;
import server.persistence.dto.DTO;
import server.persistence.dto.LoginDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Model{
    private String id;
    private String password;

    @Override
    public DTO toDTO() {
        return new LoginDTO(id, password);
    }
}
