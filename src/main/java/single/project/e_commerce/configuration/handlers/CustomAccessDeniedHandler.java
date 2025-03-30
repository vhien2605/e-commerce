package single.project.e_commerce.configuration.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import single.project.e_commerce.dto.response.ApiErrorResponse;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.io.IOException;


@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("-----------------------------access denied handler start--------------------------------");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        ApiErrorResponse apiResponse = ApiErrorResponse.builder()
                .message(ErrorCode.ACCESS_DENIED.getMessage())
                .status(ErrorCode.ACCESS_DENIED.getCode())
                .error(ErrorCode.ACCESS_DENIED.name())
                .path(request.getRequestURI())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
