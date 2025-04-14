package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CategoryResponseDTO implements Serializable {
    private long id;
    private String name;
    private String description;
}
