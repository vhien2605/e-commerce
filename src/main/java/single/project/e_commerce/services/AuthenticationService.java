package single.project.e_commerce.services;


import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.response.TokenResponseDTO;
import single.project.e_commerce.exceptions.AuthenticationException;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.security.SecurityUser;
import single.project.e_commerce.utils.enums.TokenType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public TokenResponseDTO authenticate(LoginRequestDTO request) {
        User user = userRepository.findByUsername(
                        request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Bad Credentials!"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Bad Credentials!");
        }
        String accessToken = jwtService.generateToken(new SecurityUser(user), TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(new SecurityUser(user), TokenType.REFRESH);
        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
