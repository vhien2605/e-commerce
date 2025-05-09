package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class PaymentSuccessResponseDTO implements Serializable {
    private List<Long> orderIds;
    private String message;
}
