package single.project.e_commerce.dto.request;


import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class QuantityRequestDTO {
    @Min(value = 0, message = "must not be negative")
    private long remainingQuantity;
    
    @Min(value = 0, message = "must not be negative")
    private long soldQuantity;
}
