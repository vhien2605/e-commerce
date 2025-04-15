package single.project.e_commerce.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.configuration.securityModels.SecurityUser;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.TokenType;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    @Value("${security.jwt.access.timeout}")
    private long accessTimeOut;
    @Value("${security.jwt.refresh.timeout}")
    private long refreshTimeOut;
    @Value("${security.jwt.reset.timeout}")
    private long resetTimeOut;


    @Value("${security.jwt.access.key}")
    private String accessKey;
    @Value("${security.jwt.refresh.key}")
    private String refreshKey;
    @Value("${security.jwt.reset.key}")
    private String resetKey;

    private final RedisTokenService redisTokenService;
    private final TokenService tokenService;

    public String generateToken(SecurityUser user, TokenType type) {
        return generateToken(Map.of("userId", buildScopes(user)), user, type);
    }

    public void validateToken(String token, TokenType type) {
        var claims = extractAllClaim(token, type);  // if signature not match and other invalids -> throw exception
        checkExpired(token, type);
        checkDisable(token, type);
    }

    public void checkDisable(String token, TokenType type) {
        if (type.equals(TokenType.ACCESS)) {
            String jti = extractId(token, type);
            var redisToken = redisTokenService.findByJti(jti);
            if (redisToken.isPresent()) {
                throw new AppException(ErrorCode.TOKEN_BLACK_LIST);
            }
        } else if (type.equals(TokenType.REFRESH)) {
            String jti = extractId(token, type);
            var dbToken = tokenService.findById(jti);
            if (dbToken.isEmpty()) {
                throw new AppException(ErrorCode.TOKEN_IS_DISABLE);
            }
        }
    }

    public void checkExpired(String token, TokenType type) {
        if (extractExpiration(token, type).before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED);
        }
    }

    private String generateToken(Map<String, Object> claims, SecurityUser user, TokenType type) {
        log.info("------------------------- start generating token ------------------------------");
        long expireTime;
        if (type.equals(TokenType.ACCESS)) {
            expireTime = 1000L * 60 * 60 * accessTimeOut;
        } else if (type.equals(TokenType.REFRESH)) {
            expireTime = 1000L * 60 * 60 * 24 * 30 * refreshTimeOut;
        } else {
            expireTime = 1000L * 60 * refreshTimeOut;
        }
        return Jwts.builder()
                .setClaims(claims)
                .setId(String.valueOf(UUID.randomUUID()))
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getKey(type), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if (type.equals(TokenType.REFRESH)) {
            keyBytes = Decoders.BASE64.decode(refreshKey);
        } else if (type.equals(TokenType.ACCESS)) {
            keyBytes = Decoders.BASE64.decode(accessKey);
        } else if (type.equals(TokenType.RESET)) {
            keyBytes = Decoders.BASE64.decode(resetKey);
        } else {
            throw new AppException(ErrorCode.TOKEN_TYPE_INVALID);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private List<String> buildScopes(SecurityUser user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }


    public String extractId(String token, TokenType type) {
        return extractClaim(token, type, Claims::getId);
    }

    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    public Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsFunction) {
        final Claims claim = extractAllClaim(token, type);
        return claimsFunction.apply(claim);
    }

    private Claims extractAllClaim(String token, TokenType type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }
}
