package single.project.e_commerce.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequestDTO request) {
        return "login";
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
