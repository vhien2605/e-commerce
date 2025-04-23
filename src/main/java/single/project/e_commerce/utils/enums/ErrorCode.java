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
    RESOURCE_NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),
    TOKEN_INVALID(401, "Token is invalid", HttpStatus.UNAUTHORIZED),
    TOKEN_IS_DISABLE(401, "Token is disable", HttpStatus.UNAUTHORIZED),
    TOKEN_BLACK_LIST(401, "Token is in BlackList", HttpStatus.UNAUTHORIZED),
    TOKEN_IS_EXPIRED(401, "Token is expired", HttpStatus.UNAUTHORIZED),
    NEW_PASSWORD_EXISTED(400, "password is existed, please enter other password", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_NOT_MATCHED(400, "confirm password does not matched", HttpStatus.BAD_REQUEST),
    JSON_INVALID(400, "input json is invalid", HttpStatus.BAD_REQUEST),
    FILE_STORAGE_SERVICE_UNAVAILABLE(503, "sub service of system is unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    PRODUCT_NOT_EXIST(400, "product is not exist", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_EXIST(400, "review is not exist", HttpStatus.BAD_REQUEST),
    SHOP_NOT_EXIST(400, "shop with owned user's username is not exist", HttpStatus.BAD_REQUEST),
    SELLER_REGISTERED(400, "you are already a seller", HttpStatus.BAD_REQUEST),
    CART_NOT_EXIST(400, "user cart is not exist", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
