package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Builder
public class CartUpdateRequestDTO {
    @NotEmpty(message = "update set must not be empty")
    private Set<CartItemUpdateRequestDTO> updatedCartItems;
}
