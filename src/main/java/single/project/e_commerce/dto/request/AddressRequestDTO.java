package single.project.e_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class AddressRequestDTO implements Serializable {
    @NotBlank(message = "Address's name must not be blank!")
    private String name;

    @NotBlank(message = "city's name must not be blank!")
    private String city;

    @NotBlank(message = "country's name must not be blank!")
    private String country;
}
