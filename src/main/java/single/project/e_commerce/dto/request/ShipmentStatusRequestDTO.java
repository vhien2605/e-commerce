package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.ShippingStatus;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class ShipmentStatusRequestDTO implements Serializable {
    @NotNull(message = "shipment id is required")
    private Long id;

    @NotNull(message = "shipping status is required")
    @EnumPattern(name = "ship pattern", regexp = "SHIPPING|SHIPPED|RETURNING", message = "enum invalid")
    private ShippingStatus status;
}
