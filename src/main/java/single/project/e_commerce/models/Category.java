package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "categories")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotBlank(message = "name must not be blank")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Description must not be blank")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;
}
