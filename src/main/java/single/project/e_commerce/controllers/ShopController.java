package single.project.e_commerce.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.ShopRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.dto.response.ShopInformationResponseDTO;
import single.project.e_commerce.services.ShipmentService;
import single.project.e_commerce.services.ShopService;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Validated
public class ShopController {
    private final ShopService shopService;
    private final ShipmentService shipmentService;

    @PostMapping("/register-shop")
    public ApiResponse registerShop(@RequestBody @Valid ShopRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .data(shopService.registerShop(dto))
                .status(HttpStatus.OK.value())
                .message("register shop successfully!, now you became a seller, please check your shop information again")
                .build();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getAllShops() {
        return ApiSuccessResponse.builder()
                .data(shopService.getAllShops())
                .status(HttpStatus.OK.value())
                .message("Get all shops successfully!")
                .build();
    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('read_shop')")
    public ApiResponse shopDetail(@RequestParam(name = "shopId", defaultValue = "0") @Min(value = 0,
                                          message = "shop must be greater than 0") Long shopId,
                                  @RequestParam(name = "productId", required = false) @Min(value = 0,
                                          message = "shop must be greater than 0") Long productId) {
        return ApiSuccessResponse.builder()
                .data((productId == null)
                        ? shopService.getShopInformationById(shopId)
                        : shopService.getInformationByProductId(productId))
                .status(HttpStatus.OK.value())
                .message("Get shop detail information successfully!")
                .build();
    }


    @GetMapping("/my-shop/detail")
    @PreAuthorize("hasRole('SELLER') OR hasRole('ADMIN')")
    public ApiResponse myShop() {
        return ApiSuccessResponse.builder()
                .data(shopService.getMyShop())
                .status(HttpStatus.OK.value())
                .message("Get shop detail information successfully!")
                .build();
    }


    @GetMapping("/my-shop/shipments")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse getShopShipments() {
        return ApiSuccessResponse.builder()
                .data(shipmentService.getShopShipment())
                .status(HttpStatus.OK.value())
                .message("Get all shop shipments successfully")
                .build();
    }
}
