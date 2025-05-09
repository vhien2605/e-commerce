package single.project.e_commerce.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
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


    @ExceptionHandler(value = {AppException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse appException(AppException e, WebRequest request) {
        log.info("---------------------------Application exception handler start---------------------------");
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(e.getError().getCode())
                .message(e.getError().getMessage())
                .error(e.getError().name())
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse serverException(Exception e, WebRequest request) {
        log.info("---------------------------500 server exception handler start---------------------------");
        return ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .error(e.toString())
                .path(request.getDescription(false))
                .build();
    }
}
