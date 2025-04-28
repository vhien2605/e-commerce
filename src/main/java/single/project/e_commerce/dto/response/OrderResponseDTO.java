package single.project.e_commerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.models.OrderDetail;
import single.project.e_commerce.models.Payment;
import single.project.e_commerce.utils.annotations.PhoneNumber;
import single.project.e_commerce.utils.enums.OrderStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Builder
public class OrderResponseDTO implements Serializable {
    private long id;
    private String receiverAddress;
    private String receiverNumber;
    private double totalPrice;
    private OrderStatus status;
    private Date orderAt;
    private Set<OrderDetailResponseDTO> orderDetails;
}
