package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class PaymentRequestDTO implements Serializable {
    @NotEmpty(message = "order ids must not be empty")
    private Set<Long> orderIds;
    private String bankCode = "VNPAY";
}
