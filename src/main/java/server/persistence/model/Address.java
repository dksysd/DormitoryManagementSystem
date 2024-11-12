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
        return (DTO) AddressDTO.builder()
                .id(id)
                .postalCode(postalCode)
                ._do(_do)
                .si(si)
                .detailAddress(detailAddress)
                .createdAt(createdAt);
    }
}
