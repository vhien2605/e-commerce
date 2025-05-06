package single.project.e_commerce.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import single.project.e_commerce.configuration.filters.JwtFilter;
import single.project.e_commerce.configuration.filters.UrlRegistryFilter;
import single.project.e_commerce.configuration.handlers.CustomAccessDeniedHandler;
import single.project.e_commerce.configuration.handlers.CustomAuthenticationEntryPoint;
import single.project.e_commerce.services.SecurityUserDetailService;

@Configuration
@RequiredArgsConstructor
// enable method authorization
@EnableMethodSecurity
public class SecurityConfig {
    private final SecurityUserDetailService userDetailService;
    private final JwtFilter jwtFilter;
    private final UrlRegistryFilter urlRegistryFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final String[] WHITE_LIST = {
            "/api/auth/**",
            "/api/payment/vn-pay-callback"
    };

    private final String[] SYSTEM_ADMIN_LIST = {
            "/api/role/**",
            "/api/user/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(SYSTEM_ADMIN_LIST).hasRole("SYSTEM_ADMIN")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(urlRegistryFilter, JwtFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        requests -> requests
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                .build();
    }
}
