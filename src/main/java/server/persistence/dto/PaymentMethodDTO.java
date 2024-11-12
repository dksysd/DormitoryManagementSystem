package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO implements DTO {
    private Integer id;
    private String methodName;
    private String description;

    @Override
    public Model toModel() {
        return (Model) PaymentMethod.builder()
                .id(id)
                .methodName(methodName)
                .description(description);
    }
}
