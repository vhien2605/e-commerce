package single.project.e_commerce.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@SuperBuilder
public class ApiSuccessResponse<T> extends ApiResponse implements Serializable {
    private T data;
}