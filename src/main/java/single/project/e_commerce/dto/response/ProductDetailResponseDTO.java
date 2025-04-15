package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class ProductDetailResponseDTO implements Serializable {
    private long id;
    private String name;
    private double price;
    private String shortDesc;
    private String longDesc;
    private String imageUrl;
    private long remainingQuantity;
    private long soldQuantity;
    private Set<CategoryResponseDTO> categories;
    private Set<ReviewResponseDTO> reviews;
}
