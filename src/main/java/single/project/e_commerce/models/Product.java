package single.project.e_commerce.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "products")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank(message = "Product name must not be blank")
    @Column(name = "name")
    private String name;

    @Min(value = 0, message = "Price must be at least 0")
    @Column(name = "price")
    private double price;

    @NotBlank(message = "Short description must not be blank")
    @Column(name = "short_desc")
    private String shortDesc;

    @NotBlank(message = "Long description must not be blank")
    @Column(name = "long_desc", columnDefinition = "TEXT")
    private String longDesc;

    @Column(name = "image_url")
    private String imageUrl;

    @Min(value = 0, message = "Remaining quantity must be at least 0")
    @Column(name = "remaining_quantity")
    private long remainingQuantity;

    @Min(value = 0, message = "Sold quantity must be at least 0")
    @Column(name = "sold_quantity")
    private long soldQuantity;

    @OneToMany(mappedBy = "product")
    private Set<CartDetail> cartDetails;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "products_categories", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @OneToMany(mappedBy = "product")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails;
}
