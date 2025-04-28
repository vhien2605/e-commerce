package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import single.project.e_commerce.utils.annotations.PhoneNumber;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class CreateOrderRequestDTO implements Serializable {
    //    id long [primary key] auto
//    user_id long [ref: > users.id] auto
//    receiver_address string
//    receiver_number string
//    total_price double auto
//    status string auto
//    ordered_at datetime  auto
    @NotBlank(message = "address must be required")
    private String receiverAddress;

    @PhoneNumber(message = "phone number format is invalid")
    @NotBlank(message = "phone number must not be blank")
    private String receiverNumber;
}
