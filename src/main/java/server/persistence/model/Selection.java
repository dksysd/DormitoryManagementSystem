package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;
import server.persistence.dto.DTO;
import server.persistence.dto.ImageDTO;
import server.persistence.dto.SelectionApplicationStatusDTO;
import server.persistence.dto.SelectionDTO;

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

    private SelectionApplicationDTO selectionApplicationDTO;
    private ImageDTO tuberculosisCertificateFileDTO;
    private ImageDTO additionalProofFileDTO;

    @Override
    public DTO toDTO() {
        return (DTO) SelectionDTO.builder()
                .id(id)
                .isFinalApproved(isFinalApproved)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .selectionApplicationDTO(selectionApplicationDTO)
                .tuberculosisCertificateFileDTO(tuberculosisCertificateFileDTO)
                .build();
    }
}
