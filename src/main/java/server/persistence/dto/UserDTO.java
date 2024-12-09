package server.persistence.dto;

import lombok.*;
import server.persistence.model.Model;
import server.persistence.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements DTO {
    private Integer id;
    private String uid;
    private String loginPassword;
    private String userName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserTypeDTO userTypeDTO;
    private GenderCodeDTO genderCodeDTO;
    private AddressDTO addressDTO;
    private ImageDTO imageDTO;

    @Override
    public Model toModel() {
        return (Model) User.builder()
                .id(id)
                .uid(uid)
                .loginPassword(loginPassword)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .userTypeDTO(userTypeDTO)
                .genderCodeDTO(genderCodeDTO)
                .addressDTO(addressDTO)
                .imageDTO(imageDTO)
                .build();
    }
}
