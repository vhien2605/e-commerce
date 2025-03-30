package single.project.e_commerce.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
public class RoleResponseDTO implements Serializable {
    private String name;
    private Set<PermissionResponseDTO> permissions;
}
