package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Selection;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionDTO implements DTO {
    private Integer id;
    private boolean isFinalApproved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SelectionApplicationStatusDTO selectionApplicationStatusDTO;
    private ImageDTO tuberculosisCertificateFileDTO;
    private ImageDTO additionalProofFileDTO;

    @Override
    public Model toModel() {
        return (Model) Selection.builder()
                .id(id)
                .isFinalApproved(isFinalApproved)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionApplicationStatusDTO(selectionApplicationStatusDTO)
                .tuberculosisCertificateFileDTO(tuberculosisCertificateFileDTO)
                .build();
    }
}
