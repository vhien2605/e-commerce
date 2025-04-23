package single.project.e_commerce.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.AddToCartRequestDTO;
import single.project.e_commerce.dto.request.CartUpdateRequestDTO;
import single.project.e_commerce.dto.request.RemoveCartItemsRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('read_product')")
    public ApiResponse cartDetail() {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("add to cart")
                .data(cartService.getCartItems())
                .build();
    }

    @PostMapping("/add-to-cart")
    @PreAuthorize("hasAuthority('read_product')")
    public ApiResponse addToCart(@RequestBody @Valid AddToCartRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("add to cart")
                .data(cartService.addToCart(dto))
                .build();
    }

    @DeleteMapping("/remove-from-cart")
    @PreAuthorize("hasAuthority('read_product')")
    public ApiResponse removeFromCart(@RequestBody @Valid RemoveCartItemsRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("removed cart items")
                .data("rows effected : " + cartService.removeFromCart(dto))
                .build();
    }

    @PatchMapping("/update-quantity")
    public ApiResponse updateCartItems(@RequestBody @Valid CartUpdateRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("updated cart items")
                .data(cartService.updateCartItems(dto))
                .build();
    }
}
