package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShippingStatus {
    @JsonProperty("shipped")
    SHIPPED,

    @JsonProperty("shipping")
    SHIPPING,

    @JsonProperty("returning")
    RETURNING,

    UNKNOWN;

    @JsonCreator
    public static ShippingStatus from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return ShippingStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
