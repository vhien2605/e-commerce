package single.project.e_commerce.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@Builder
public class PageResponseDTO<T> implements Serializable {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private T data;
}
