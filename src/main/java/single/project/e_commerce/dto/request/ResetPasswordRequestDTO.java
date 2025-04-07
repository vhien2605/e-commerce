package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ResetPasswordRequestDTO implements Serializable {
    @Size(min = 4, message = "Password size must be greater than or equal 4!")
    private String password;
    @Size(min = 4, message = "Password size must be greater than or equal 4!")
    private String confirmPassword;
}
