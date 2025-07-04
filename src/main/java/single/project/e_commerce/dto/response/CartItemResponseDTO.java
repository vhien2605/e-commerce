package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class CartItemResponseDTO implements Serializable {
    private long id;
    private long productId;
    private long price;
    private String name;
    private String imageUrl;
    private long quantity;
}
