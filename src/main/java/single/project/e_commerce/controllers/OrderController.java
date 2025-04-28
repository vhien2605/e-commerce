package single.project.e_commerce.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.CreateOrderRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.OrderService;

@RestController
@RequestMapping("/api/order")
@Validated
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('create_order')")
    @PostMapping("/create-order")
    public ApiResponse createOrder(@RequestBody CreateOrderRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("create order successfully")
                .data(orderService.createOrder(dto))
                .build();
    }

    @PreAuthorize("hasAuthority('read_order')")
    @GetMapping("/my-orders")
    public ApiResponse getMyOrders() {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("read my orders successfully")
                .data(orderService.getMyOrders())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-orders")
    public ApiResponse getAllOrders() {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("read all orders successfully")
                .data(orderService.getAllOrders())
                .build();
    }


    @PreAuthorize("hasAuthority('delete_order')")
    public ApiResponse deleteOrder() {
        return null;
    }
}
