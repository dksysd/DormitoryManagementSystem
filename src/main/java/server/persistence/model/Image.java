package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Model {
    private Integer id;
    private String name;
    private Byte[] data;
    private Integer width;
    private Integer height;
    private String extension;

    @Override
    public DTO toDTO() {
        return (DTO) ImageDTO.builder()
                .id(id)
                .name(name)
                .data(data)
                .width(width)
                .height(height)
                .extension(extension);
    }
}
