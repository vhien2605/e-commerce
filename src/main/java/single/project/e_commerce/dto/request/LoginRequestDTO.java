package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequestDTO implements Serializable {
    @Size(min = 4, message = "Username size must be greater or equal 4!")
    private String username;

    @Size(min = 4, message = "Password must be greater or equal 4!")
    private String password;
}