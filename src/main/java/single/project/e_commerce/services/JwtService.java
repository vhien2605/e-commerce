package single.project.e_commerce.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import single.project.e_commerce.dto.response.TokenResponseDTO;
import single.project.e_commerce.exceptions.DataInvalidException;
import single.project.e_commerce.security.SecurityUser;
import single.project.e_commerce.utils.enums.TokenType;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j
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

    public String generateToken(SecurityUser user, TokenType type) {
        return generateToken(Map.of("userId", user.getAuthorities()), user, type);
    }

    private String generateToken(Map<String, Object> claims, SecurityUser user, TokenType type) {
        log.info("------------------------- start generating token ------------------------------");
        long expireTime;
        if (type.equals(TokenType.ACCESS)) {
            expireTime = 1000 * 60 * 60 * accessTimeOut;
        } else if (type.equals(TokenType.REFRESH)) {
            expireTime = 1000 * 60 * 60 * 24 * 30 * refreshTimeOut;
        } else {
            expireTime = 1000 * 60 * refreshTimeOut;
        }
        return Jwts.builder()
                .setClaims(claims)
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
            throw new DataInvalidException("token type is invalid");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimsFunction) {
        final Claims claim = extractAllClaim(token, type);
        return claimsFunction.apply(claim);
    }

    private Claims extractAllClaim(String token, TokenType type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }
}
