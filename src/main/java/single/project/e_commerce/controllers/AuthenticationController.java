package single.project.e_commerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ApiSuccessResponse;
import single.project.e_commerce.services.AuthenticationService;
import single.project.e_commerce.services.RedisTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RedisTokenService tokenService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody @Valid LoginRequestDTO request) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("Authenticated!")
                .data(authenticationService.authenticate(request))
                .build();
    }


    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        return ApiSuccessResponse.builder()
                .status(200)
                .message("Logout")
                .data(authenticationService.logout(request))
                .build();
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
