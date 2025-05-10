package single.project.e_commerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.models.OrderDetail;
import single.project.e_commerce.models.Shop;
import single.project.e_commerce.models.User;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.ShippingStatus;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Builder
public class ShipmentResponseDTO implements Serializable {
    private long id;

    @NotBlank(message = "tracking id must not be blank")
    private String trackingId;

    @NotBlank(message = "receiver address must be required")
    private String receiverAddress;


    @NotBlank(message = "receiver number must be required")
    private String receiverNumber;

    @NotNull(message = "shipping status must not be blank")
    @EnumPattern(name = "shippingStatus", regexp = "SHIPPED|SHIPPING")
    private ShippingStatus shippingStatus;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date deliveredAt;
}
