package single.project.e_commerce.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.models.User;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class ShopRequestDTO implements Serializable {
    @NotBlank(message = "shop name must be required!")
    private String name;

    @NotBlank(message = "description must be required!")
    private String description;
}
