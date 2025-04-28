package single.project.e_commerce.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderDetailResponseDTO {
    private long id;
    private long productId;
    private long price;
    private String name;
    private String imageUrl;
    private long quantity;
}
