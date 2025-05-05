package single.project.e_commerce.controllers;


import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import single.project.e_commerce.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/payment")
@Validated
public class PaymentController {
    @PostMapping("/check-out/{id}")
    public ApiResponse checkout(@PathVariable @Min(value = 0, message = "order id must be greater than 0")
                                Long orderId
    ) {
        return null;
    }
}
