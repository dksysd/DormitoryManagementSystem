package server.persistence.dto;

import lombok.*;
import server.persistence.model.Address;
import server.persistence.model.Model;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO implements DTO {
    private Integer id;
    private String postalCode;
    private String _do;
    private String si;
    private String detailAddress;
    private LocalDateTime createdAt;

    @Override
    public Model toModel() {
        return new Address(id, postalCode, _do, si, detailAddress, createdAt);
    }
}
