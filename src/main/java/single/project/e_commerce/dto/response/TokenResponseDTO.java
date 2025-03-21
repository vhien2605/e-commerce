package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class TokenResponseDTO implements Serializable {
    private String accessToken;
    private String refreshToken;
}
