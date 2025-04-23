package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class RemoveCartItemsRequestDTO implements Serializable {
    @NotEmpty(message = "removed ids must not empty")
    private Set<Long> ids;
}
