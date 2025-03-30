package single.project.e_commerce.services;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.response.TokenResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.models.RedisToken;
import single.project.e_commerce.models.Token;
import single.project.e_commerce.models.User;
import single.project.e_commerce.repositories.UserRepository;
import single.project.e_commerce.configuration.securityModels.SecurityUser;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.TokenType;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final TokenService tokenService;

    public TokenResponseDTO authenticate(LoginRequestDTO request) {
        User user = userRepository.findUserWithRoleAndPermissionByUsername(
                        request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String accessToken = jwtService.generateToken(new SecurityUser(user), TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(new SecurityUser(user), TokenType.REFRESH);


        //store refresh token in db
        Token token = Token.builder()
                .jti(jwtService.extractId(refreshToken, TokenType.REFRESH))
                .username(user.getUsername())
                .build();
        tokenService.saveToken(token);

        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String logout(HttpServletRequest request) {
        String accessToken = request.getHeader("x-token");
        String refreshToken = request.getHeader("y-token");
        // save access token to redis black list
        if (jwtService.isValid(accessToken, TokenType.ACCESS)) {
            redisTokenService.saveToken(buildRedisToken(accessToken, TokenType.ACCESS));
        }
        //remove refresh token from DB
        if (jwtService.isValid(refreshToken, TokenType.REFRESH)) {
            String jti = jwtService.extractId(refreshToken, TokenType.REFRESH);
            tokenService.deleteById(jti);
        }
        return "logout Successfully";
    }

    private RedisToken buildRedisToken(String token, TokenType type) {
        String username = jwtService.extractUsername(token, type);
        String jti = jwtService.extractId(token, type);
        Date expirationDate = jwtService.extractExpiration(token, type);
        long timeToLive = (jwtService.extractExpiration(token, type).getTime() - new Date().getTime()) / 1000;
        return RedisToken.builder()
                .jti(jti)
                .timeToLive(timeToLive)
                .username(username)
                .build();
    }
}
