package single.project.e_commerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import single.project.e_commerce.dto.response.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, DataInvalidException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiErrorResponse dataValidationException(Exception e, WebRequest request) {
        String error = e.getMessage();
        int startPrefix = error.lastIndexOf("[");
        int endPredix = error.lastIndexOf("]");
        String message = error.substring(startPrefix + 1, endPredix - 1);
        return ApiErrorResponse.builder()
                .status(400)
                .message(message)
                .error(error)
                .path(request.getDescription(false))
                .build();
    }

    @ExceptionHandler(value = {DataDuplicateException.class})
    @ResponseStatus(HttpStatus.OK)
    public ApiErrorResponse dataDuplicateException(Exception e, WebRequest request) {
        String error = e.getMessage();
        return ApiErrorResponse.builder()
                .status(409)
                .message(error)
                .error(error)
                .path(request.getDescription(false))
                .build();
    }
}
