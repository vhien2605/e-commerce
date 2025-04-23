package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class AddToCartRequestDTO implements Serializable {
    @NotNull(message = "id is require")
    private long id;
    @NotNull(message = "quantity is require")
    private long quantity;
}
