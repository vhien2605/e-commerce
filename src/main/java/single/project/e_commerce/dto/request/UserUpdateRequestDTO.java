package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.Gender;
import single.project.e_commerce.utils.enums.Status;

import java.io.Serializable;
import java.util.Set;


@Setter
@Getter
@Builder
public class UserUpdateRequestDTO implements Serializable {
    @NotNull(message = "Username must not be null!")
    @Size(min = 4, message = "Username size must be greater than or equal 4!")
    private String username;

    @NotNull(message = "Password must not be null!")
    @Size(min = 4, message = "Password size must be greater than or equal 4!")
    private String password;

    @NotBlank(message = "Email must not be null!")
    @Email(message = "Input is not email type!")
    private String email;

    @NotNull(message = "Address must not be null!")
    private AddressRequestDTO address;

    @EnumPattern(name = "gender", regexp = "MALE|FEMALE")
    private Gender gender;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|LOCKED")
    private Status status;

    private Set<String> roles;
}
