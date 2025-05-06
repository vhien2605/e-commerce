package single.project.e_commerce.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("male")
    MALE,

    @JsonProperty("female")
    FEMALE,

    UNKNOWN;

    @JsonCreator
    public static Gender from(String value) {
        if (value == null) return UNKNOWN;
        try {
            return Gender.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
