package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class AddressFilterResponseDTO implements Serializable {
    private String name;
    private String city;
    private String country;
}
