package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.Min;
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
public class ProductUpdateRequestDTO implements Serializable {
    @NotBlank(message = "Product name must not be blank")
    private String name;

    @Min(value = 0, message = "Price must be at least 0")
    private double price;

    @NotBlank(message = "Short description must not be blank")
    private String shortDesc;

    @NotBlank(message = "Long description must not be blank")
    private String longDesc;

    @Min(value = 0, message = "Remaining quantity must be at least 0")
    private long remainingQuantity;

    @Min(value = 0, message = "Sold quantity must be at least 0")
    private long soldQuantity;

    @NotEmpty(message = "categories must not be empty")
    private Set<CategoryRequestDTO> categories;
}
