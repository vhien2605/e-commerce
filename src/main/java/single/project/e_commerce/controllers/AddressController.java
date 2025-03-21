package single.project.e_commerce.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import single.project.e_commerce.dto.request.AddressRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.AddressService;


@RestController
@RequestMapping("/address/manager")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;


    @GetMapping("/users")
    @PreAuthorize("read_user")
    public ApiResponse getUsersInLocation(@RequestBody AddressRequestDTO request) {
        return ApiSuccessResponse.builder()
                .data(addressService.getAddressWithUsersByLocation(request))
                .status(200)
                .message("Get all users by location successfully!")
                .build();
    }
}
