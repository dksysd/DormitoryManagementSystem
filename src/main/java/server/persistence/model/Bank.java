package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank implements Model {
    private Integer id;
    private String bankName;
    private String bankCode;

    @Override
    public DTO toDTO() {
        return (DTO) BankDTO.builder()
                .id(id)
                .bankName(bankName)
                .bankCode(bankCode);
    }
}
