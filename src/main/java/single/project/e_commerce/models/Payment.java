package single.project.e_commerce.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    //    id long [primary key]
//    order_id long [ref: - orders.id]
//    user_id long [ref: > users.id]
//    total_price bigint
//    payment_method string
//    paid_at datetime
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "price")
    @Min(value = 0, message = "price must be greater than 0")
    private double price;

    @Column(name = "payment_method")
    @NotBlank(message = "payment method is required")
    private String paymentMethod;

    @Column(name = "paid_at")
    @NotNull(message = "paid date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date paidAt;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
