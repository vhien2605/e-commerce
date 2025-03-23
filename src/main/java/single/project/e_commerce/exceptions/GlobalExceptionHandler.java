package single.project.e_commerce.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import single.project.e_commerce.dto.response.ApiErrorResponse;
import single.project.e_commerce.dto.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, DataInvalidException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse dataValidationException(Exception e, WebRequest request) {
        log.info("--------------------data validation exception handler start--------------------");
        String error = e.getMessage();
        int startPrefix = error.lastIndexOf("[");
        int endPredix = error.lastIndexOf("]");
        String message = error.substring(startPrefix + 1, endPredix - 1);
        return ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .error(error)
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(value = {DataDuplicateException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse dataDuplicateException(Exception e, WebRequest request) {
        log.info("--------------------data duplicate exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("data input duplicated")
                .error(error)
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse authenticationHandler(Exception e, WebRequest request) {
        log.info("------------------------------authentication exception handler start----------------------------");
        return ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("some thing wrong with your authentication")
                .error(e.getMessage())
                .path(request.getDescription(false))
                .build();
    }
    
}
