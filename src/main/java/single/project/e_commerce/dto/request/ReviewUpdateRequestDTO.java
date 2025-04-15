package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class ReviewUpdateRequestDTO {
    @NotNull(message = "this must be required")
    private int rate;
    @NotBlank(message = "title must be required")
    private String title;
    @NotBlank(message = "description must be required")
    private String description;
}
