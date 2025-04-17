package single.project.e_commerce.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RegisterShopResponseDTO implements Serializable {
    private long id;
    private String name;
    private String description;
}
