package single.project.e_commerce.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.AddressService;


@RestController
@RequestMapping("/api/address/manager")
@RequiredArgsConstructor
@Validated
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('read_user')")
    public ApiResponse getUsersInLocation(@RequestParam("name") String name,
                                          @RequestParam("city") String city,
                                          @RequestParam("country") String country
    ) {
        return ApiSuccessResponse.builder()
                .data(addressService.getAddressWithUsersByLocation(name, city, country))
                .status(HttpStatus.OK.value())
                .message("Get all users by location successfully!")
                .build();
    }

    @GetMapping("/all-addresses")
    public ApiResponse getAllAddress() {
        return ApiSuccessResponse.builder()
                .data(addressService.getAllAddress())
                .status(HttpStatus.OK.value())
                .message("Get all addresses successfully!")
                .build();
    }

    @GetMapping("/all-addresses/advanced-filter")
    public ApiResponse advancedFilterAddress(
            Pageable pageable,
            @RequestParam(name = "address", required = false) String[] address,
            @RequestParam(name = "sortBy", defaultValue = "id:asc") String[] sortBy) {
        return ApiSuccessResponse.builder()
                .data(addressService.getAllAddressByFilterSpecification(pageable, address, sortBy))
                .status(HttpStatus.OK.value())
                .message("Get filtered addresses successfully!")
                .build();
    }

    @GetMapping("all-addresses/filter")
    public ApiResponse advancedFilter(
            @RequestParam(name = "address", required = false) String[] address,
            @RequestParam(name = "sortBy", defaultValue = "id:asc") String[] sortBy
    ) {
        return ApiSuccessResponse.builder()
                .data(addressService.getAllAddressBySpecificationNotPagination(address, sortBy))
                .status(HttpStatus.OK.value())
                .message("Get filtered addresses successfully!")
                .build();
    }
}
