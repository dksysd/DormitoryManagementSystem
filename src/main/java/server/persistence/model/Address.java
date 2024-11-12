package server.persistence.model;

import lombok.*;
import server.persistence.dto.AddressDTO;
import server.persistence.dto.DTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Model {
    private Integer id;
    private String postalCode;
    private String _do;
    private String si;
    private String detailAddress;
    private LocalDateTime createdAt;

    @Override
    public DTO toDTO() {
        return new AddressDTO(id, postalCode, _do, si, detailAddress, createdAt);
    }
}
