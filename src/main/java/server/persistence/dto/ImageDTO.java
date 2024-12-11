package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.Image;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO implements DTO {
    private Integer id;
    private String name;
    private Byte[] data;
    private Integer width;
    private Integer height;
    private String extension;

    private UserDTO userDTO;
    @Override
    public Model toModel() {
        return (Model) Image.builder()
                .id(id)
                .name(name)
                .data(data)
                .width(width)
                .height(height)
                .extension(extension)
                .userDTO(userDTO)
                .build();
    }
}
