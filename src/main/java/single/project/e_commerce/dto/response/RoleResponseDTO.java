package single.project.e_commerce.dto.response;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleResponseDTO implements Serializable {
    private String name;
}
