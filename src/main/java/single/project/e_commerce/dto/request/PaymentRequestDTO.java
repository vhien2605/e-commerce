package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.PaymentMethod;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class PaymentRequestDTO implements Serializable {
    @NotEmpty(message = "order ids must not be empty")
    private Set<Long> orderIds;

    @NotNull(message = "bank code must be required")
    private String bankCode;

    @NotNull
    @EnumPattern(name = "payment method", regexp = "VNPAY|PAY_WHEN_RECEIVED")
    private PaymentMethod paymentMethod;
}
