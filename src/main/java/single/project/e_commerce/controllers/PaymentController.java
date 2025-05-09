package single.project.e_commerce.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.AfterPaymentRequestDTO;
import single.project.e_commerce.dto.request.PaymentRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.PaymentService;

@RestController
@RequestMapping("/api/payment")
@Validated
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/check-out")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('CUSTOMER')")
    public ApiResponse checkout(
            @RequestBody @Valid PaymentRequestDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("create url payment successfully")
                .data(paymentService.checkout(dto, request, response))
                .build();
    }


    @GetMapping("/vn-pay-callback")
    public ApiResponse payCallbackHandler(@RequestParam(name = "vnp_ResponseCode", defaultValue = "99") String status,
                                          @CookieValue("orderIds") String orderIds,
                                          @CookieValue("token") String token,
                                          HttpServletResponse response
    ) {
        if (status.equals("00")) {
            return ApiSuccessResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("success")
                    .data(paymentService.success(orderIds, token, response))
                    .build();
        } else {
            return ApiSuccessResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("failed")
                    .data(null)
                    .build();
        }
    }

    @PostMapping("/create-payment-and-shipping")
    public ApiResponse createPaymentAndShipment(@RequestBody @Valid AfterPaymentRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("create payment and shipping successfully")
                .data(paymentService.createPaymentAndShipping(dto))
                .build();
    }
}
