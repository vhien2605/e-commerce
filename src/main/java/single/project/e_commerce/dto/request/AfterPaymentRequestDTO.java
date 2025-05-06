package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class AfterPaymentRequestDTO implements Serializable {

    @NotEmpty(message = "this list must not be empty")
    private List<Long> orderIds;

    @NotBlank(message = "this must not be blank")
    private String token;
}
