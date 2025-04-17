package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ShippingStatus {
    @JsonProperty("shipped")
    SHIPPED,

    @JsonProperty("shipping")
    SHIPPING,
}
