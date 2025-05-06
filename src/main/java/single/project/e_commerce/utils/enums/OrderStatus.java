package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.redis.connection.SortParameters;

public enum OrderStatus {
    @JsonProperty("unpaid")
    UNPAID,

    @JsonProperty("paid")
    PAID,

    @JsonProperty("cancelled")
    CANCELLED,

    UNKNOWN;

    @JsonCreator
    public static OrderStatus from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return OrderStatus.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
