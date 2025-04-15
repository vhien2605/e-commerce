package single.project.e_commerce.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class ReviewResponseDTO implements Serializable {
    private long id;
    private int rate;
    private String title;
    private String description;
    private String username;
    private long productId;
}
