package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends AbstractEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rate")
    @NotNull(message = "this must be required")
    private int rate;

    @Column(name = "title")
    @NotBlank(message = "title field must not be blank")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotBlank(message = "description must not be blank")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
