package single.project.e_commerce.configuration.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import single.project.e_commerce.dto.response.ApiErrorResponse;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UrlRegistryFilter extends OncePerRequestFilter {
    private final RequestMappingHandlerMapping handlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------------------------URL invalid filter start------------------------------");
        String requestUri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isRegisteredRoute = handlerMapping.getHandlerMethods()
                .keySet().stream().flatMap(i -> i.getPatternValues().stream())
                .anyMatch(stringUrl -> antPathMatcher.match(stringUrl, requestUri));
        if (!isRegisteredRoute) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.OK.value());
            ApiErrorResponse apiResponse = ApiErrorResponse.
                    builder()
                    .message(ErrorCode.RESOURCE_NOT_FOUND.getMessage())
                    .status(ErrorCode.RESOURCE_NOT_FOUND.getCode())
                    .error(ErrorCode.RESOURCE_NOT_FOUND.name())
                    .path(request.getRequestURI())
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            response.flushBuffer();
            return;
        }
        filterChain.doFilter(request, response);
    }
}
