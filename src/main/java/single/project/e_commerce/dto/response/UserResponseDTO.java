package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.Status;

import java.io.Serializable;
import java.util.Set;


@Getter
@Builder
public class UserResponseDTO implements Serializable {
    private long id;
    private String fullName;
    private String username;
    private String email;
    private Gender gender;
    private Status status;
    private Set<RoleResponseDTO> roles;
}
