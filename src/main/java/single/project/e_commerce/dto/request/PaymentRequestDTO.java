package single.project.e_commerce.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Builder
public class PaymentRequestDTO implements Serializable {
    private Set<Long> orderIds;
}
