package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CartItemUpdateRequestDTO implements Serializable {
    @NotNull(message = "id must be required")
    private Long id;
    @NotNull(message = "quantity must be required")
    private Long quantity;
}
