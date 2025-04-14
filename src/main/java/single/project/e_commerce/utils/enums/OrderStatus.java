package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {

    @JsonProperty("unpaid")
    UNPAID,

    @JsonProperty("paid")
    PAID,

    @JsonProperty("shipped")
    SHIPPED,

    @JsonProperty("cancelled")
    CANCELLED
}
