package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Bank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO implements DTO {
    private Integer id;
    private String bankName;
    private String bankCode;

    @Override
    public Model toModel() {
        return (Model) Bank.builder()
                .id(id)
                .bankName(bankName)
                .bankCode(bankCode);
    }
}
