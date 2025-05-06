package single.project.e_commerce.configuration.filters;


import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import single.project.e_commerce.configuration.securityModels.SecurityUser;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.services.SecurityUserDetailService;
import single.project.e_commerce.services.JwtService;
import single.project.e_commerce.utils.enums.TokenType;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final SecurityUserDetailService securityUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring("Bearer ".length());
        try {
            jwtService.validateToken(token, TokenType.ACCESS);
            log.info("-------------------------start create UserDetails and set in Context---------------------");
            String username = jwtService.extractUsername(token, TokenType.ACCESS);
            SecurityUser user = (SecurityUser) securityUserDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AppException e) {
            log.info("-------------------------------------jwt token exception--------------------------------");
            log.info("-------------------------------------" + e.getMessage() + "--------------------------------------");
        }
        filterChain.doFilter(request, response);
    }
}
