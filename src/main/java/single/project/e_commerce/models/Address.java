package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import single.project.e_commerce.repositories.specifications.SupportsSpecification;

import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "addresses")
public class Address extends AbstractEntity implements SupportsSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @NotBlank(message = "Address's name must not be blank!")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "city's name must not be blank!")
    @Column(name = "city")
    private String city;

    @NotBlank(message = "country's name must not be blank!")
    @Column(name = "country")
    private String country;


    @OneToMany(mappedBy = "address")
    private Set<User> users;
}
