package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "carts_details")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private long quantity;

    @Min(value = 0, message = "Price must be non-negative")
    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
