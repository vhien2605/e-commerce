package single.project.e_commerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.dto.response.TokenResponseDTO;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid LoginRequestDTO request) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("Authenticated!")
                .data(authenticationService.authenticate(request))
                .build();
    }


    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }


    @PostMapping("/refresh")
    public String refresh() {
        return "refresh";
    }


    @PostMapping("/forgot-password")
    public String forgot() {
        return "forgot";
    }


    @PutMapping("/change-password")
    public String change() {
        return "password changed";
    }
}
