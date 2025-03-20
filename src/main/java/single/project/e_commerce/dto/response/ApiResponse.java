package single.project.e_commerce.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Setter
@Getter
public abstract class ApiResponse {
    protected int status;
    protected String message;
}
