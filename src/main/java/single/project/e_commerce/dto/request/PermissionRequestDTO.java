package single.project.e_commerce.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PermissionRequestDTO implements Serializable {
    private String name;
}
