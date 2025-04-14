package single.project.e_commerce.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CategoryRequestDTO implements Serializable {
    private String name;
    private String description;
}
