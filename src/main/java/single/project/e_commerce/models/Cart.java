package single.project.e_commerce.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "carts")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private Set<CartDetail> cartDetails;
}
