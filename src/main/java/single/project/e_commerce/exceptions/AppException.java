package single.project.e_commerce.exceptions;

import lombok.Getter;
import single.project.e_commerce.utils.enums.ErrorCode;


@Getter
public class AppException extends RuntimeException {
    private final ErrorCode error;

    public AppException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }
}
