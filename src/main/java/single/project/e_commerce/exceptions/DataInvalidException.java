package single.project.e_commerce.exceptions;

import single.project.e_commerce.utils.enums.ErrorCode;

public class DataInvalidException extends RuntimeException {
    public DataInvalidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
