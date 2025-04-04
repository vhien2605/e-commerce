package single.project.e_commerce.utils.enums;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(409, "User existed", HttpStatus.CONFLICT),
    USER_NOT_EXIST(400, "User not existed", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(403, "Access denied", HttpStatus.FORBIDDEN),
    ROLE_EXISTED(409, "Role existed", HttpStatus.CONFLICT),
    ROLE_NOT_EXISTED(400, "Role not existed", HttpStatus.BAD_REQUEST),
    AUTHORITY_TYPE_INVALID(400, "Authority invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXIST(400, "Address not existed", HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_INVALID(400, "Token type is invalid", HttpStatus.BAD_REQUEST),
    REGEX_INVALID(400, "Given regex is invalid", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
