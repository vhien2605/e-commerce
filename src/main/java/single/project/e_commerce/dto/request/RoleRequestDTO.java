package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Setter
@Getter
@Builder
public class RoleRequestDTO implements Serializable {
    @NotBlank(message = "Role's name must not be blank!")
    private String name;


    @NotEmpty(message = "Permissions mut not be empty!")
    private Set<String> permissions;
}
