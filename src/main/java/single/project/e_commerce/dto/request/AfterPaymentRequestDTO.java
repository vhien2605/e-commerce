package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.PaymentMethod;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class AfterPaymentRequestDTO implements Serializable {
    @NotEmpty(message = "this list must not be empty")
    private List<Long> orderIds;
    
    @NotNull
    @EnumPattern(name = "payment method", regexp = "VNPAY|PAY_WHEN_RECEIVED")
    private PaymentMethod paymentMethod;
}
