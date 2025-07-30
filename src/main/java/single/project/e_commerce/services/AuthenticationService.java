package single.project.e_commerce.services;


import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.request.LoginRequestDTO;
import single.project.e_commerce.dto.request.ResetPasswordRequestDTO;
import single.project.e_commerce.dto.response.ApiResponse;
import single.project.e_commerce.dto.response.ForgotPasswordResponseDTO;
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
@Slf4j
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final TokenService tokenService;
    private final UserService userService;

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
        // save access token to redis black list if access token valid
        jwtService.validateToken(accessToken, TokenType.ACCESS);
        redisTokenService.saveToken(buildRedisToken(accessToken, TokenType.ACCESS));


        //remove refresh token from DB if refresh token valid
        jwtService.validateToken(refreshToken, TokenType.REFRESH);
        String jti = jwtService.extractId(refreshToken, TokenType.REFRESH);
        tokenService.deleteById(jti);
        return "logout Successfully";
    }

    private RedisToken buildRedisToken(String token, TokenType type) {
        String username = jwtService.extractUsername(token, type);
        String jti = jwtService.extractId(token, type);
        Date expirationDate = jwtService.extractExpiration(token, type);
        long timeToLive = (expirationDate.getTime() - new Date().getTime()) / 1000;
        return RedisToken.builder()
                .jti(jti)
                .timeToLive(timeToLive)
                .username(username)
                .build();
    }

    public TokenResponseDTO refresh(HttpServletRequest request) {
        log.info("refresh token service");
        String token = request.getHeader("x-token");
        jwtService.validateToken(token, TokenType.REFRESH);
        String username = jwtService.extractUsername(token, TokenType.REFRESH);
        User user = userRepository.findUserWithRoleAndPermissionByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        String accessToken = jwtService.generateToken(new SecurityUser(user), TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(new SecurityUser(user), TokenType.REFRESH);
        return TokenResponseDTO.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    public String reset(HttpServletRequest request, ResetPasswordRequestDTO dto) {
        String resetToken = request.getHeader("x-token");
        String confirmPassword = dto.getConfirmPassword();
        String newPassword = dto.getPassword();
        if (!newPassword.equals(confirmPassword)) {
            throw new AppException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCHED);
        }
        jwtService.validateToken(resetToken, TokenType.RESET);
        String username = jwtService.extractUsername(resetToken, TokenType.RESET);
        return userService.changeUserPassword(username, newPassword);
    }

    public ForgotPasswordResponseDTO forgot(HttpServletRequest request) {
        String resetEmail = request.getHeader("reset-email");
        User user = userRepository.findUserWithRoleAndPermissionByEmail(resetEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        String resetToken = jwtService.generateToken(new SecurityUser(user), TokenType.RESET);
        return ForgotPasswordResponseDTO.builder()
                .resetToken(resetToken)
                .build();
    }
}
