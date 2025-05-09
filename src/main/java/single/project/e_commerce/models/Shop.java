package single.project.e_commerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "shops")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    @NotBlank(message = "name must be required")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotBlank(message = "description must be required")
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shop")
    private Set<Product> products;


    @OneToMany(mappedBy = "shop")
    private Set<Shipment> shipments;
}
