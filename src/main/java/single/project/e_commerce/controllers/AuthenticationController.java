package single.project.e_commerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.request.ResetPasswordRequestDTO;
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
                .status(HttpStatus.OK.value())
                .message("Authenticated!")
                .data(authenticationService.authenticate(request))
                .build();
    }


    @PostMapping("/logout")
    public ApiResponse logout(HttpServletRequest request) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Logout")
                .data(authenticationService.logout(request))
                .build();
    }


    @PostMapping("/refresh")
    public ApiResponse refresh(HttpServletRequest request) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("refresh token successfully!")
                .data(authenticationService.refresh(request))
                .build();
    }


    @PostMapping("/forgot-password")
    public ApiResponse forgot(HttpServletRequest request) {
        return ApiSuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("reset token is created successfully")
                .data(authenticationService.forgot(request))
                .build();
    }


    @PatchMapping("/reset-password")
    public String change(HttpServletRequest request
            , @RequestBody @Valid ResetPasswordRequestDTO requestDTO) {
        return authenticationService.reset(request, requestDTO);
    }
}
