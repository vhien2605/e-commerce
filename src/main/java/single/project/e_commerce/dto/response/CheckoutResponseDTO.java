package single.project.e_commerce.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.enums.PaymentMethod;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class CheckoutResponseDTO implements Serializable {
    private PaymentMethod paymentMethod;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;

    @NotEmpty(message = "orderIds list must not be empty")
    private List<Long> orderIds;
    
    @NotBlank(message = "message must be required not blank")
    private String message;
}
