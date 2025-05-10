package single.project.e_commerce.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.ShipmentStatusRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.ShipmentService;

@RestController
@Slf4j
@RequestMapping("/api/shipment")
@RequiredArgsConstructor
public class ShipmentController {
    private final ShipmentService shipmentService;

    @GetMapping("/my-shipments")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse getMyShipments() {
        return ApiSuccessResponse.builder()
                .data(shipmentService.getUserShipment())
                .status(HttpStatus.OK.value())
                .message("Get my shipment successfully")
                .build();
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse updateShipment(@RequestBody @Valid ShipmentStatusRequestDTO dto) {
        return ApiSuccessResponse.builder()
                .data(shipmentService.updateShipment(dto))
                .status(HttpStatus.OK.value())
                .message("update shipped successfully")
                .build();
    }
}
