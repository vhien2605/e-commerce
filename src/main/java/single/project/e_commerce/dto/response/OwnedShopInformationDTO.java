package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@Builder
public class OwnedShopInformationDTO implements Serializable {
    private long id;
    private String username;
    private String email;
}
