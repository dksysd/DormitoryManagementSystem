package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Selection implements Model {
    private Integer id;
    private boolean isFinalApproved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private SelectionApplicationStatusDTO selectionApplicationStatusDTO;
    private ImageDTO tuberculosisCertificateFileDTO;
    private ImageDTO additionalProofFileDTO;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionDTO.builder()
                .id(id)
                .isFinalApproved(isFinalApproved)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionApplicationStatusDTO(selectionApplicationStatusDTO)
                .tuberculosisCertificateFileDTO(tuberculosisCertificateFileDTO);
    }
}
