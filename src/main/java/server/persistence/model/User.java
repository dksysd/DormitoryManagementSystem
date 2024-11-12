package server.persistence.model;

import lombok.*;
import server.persistence.dto.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Model {
    private Integer id;
    private String uid;
    private String loginId;
    //    private String loginPassword;
    private String userName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserTypeDTO userTypeDTO;
    private GenderCodeDTO genderCodeDTO;
    private AddressDTO addressDTO;

    @Override
    public DTO toDTO() {
        return new UserDTO(id, uid, loginId, userName, phoneNumber, createdAt, updatedAt, userTypeDTO, genderCodeDTO, addressDTO);
    }
}
