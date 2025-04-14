package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "orders_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity")
    private long quantity;

    @Min(value = 0, message = "Price must be non-negative")
    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
