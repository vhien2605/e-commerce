package single.project.e_commerce.dto.response;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
public class AddressResponseDTO implements Serializable {
    private String name;
    private String city;
    private String country;
    private Set<UserResponseDTO> users;
}
