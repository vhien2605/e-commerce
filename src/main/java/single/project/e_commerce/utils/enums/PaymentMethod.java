package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentMethod {
    @JsonProperty("vnpay")
    VNPAY,

    @JsonProperty("pay_when_received")
    PAY_WHEN_RECEIVED,

    UNKNOWN;

    @JsonCreator
    public static PaymentMethod from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return PaymentMethod.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
