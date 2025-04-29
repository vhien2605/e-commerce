package single.project.e_commerce.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import single.project.e_commerce.repositories.specifications.SupportsSpecification;
import single.project.e_commerce.utils.annotations.PhoneNumber;
import single.project.e_commerce.utils.enums.OrderStatus;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractEntity implements SupportsSpecification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "receiver_address")
    @NotBlank(message = "receiver address must be required")
    private String receiverAddress;

    @Column(name = "receiver_number")
    @NotBlank(message = "receiver number must be required")
    @PhoneNumber(message = "phone number is invalid")
    private String receiverNumber;

    @Column(name = "total_price")
    @Min(value = 0, message = "value must be greater than 0")
    private double totalPrice;

    @Column(name = "status")
    @NotNull(message = "status must be required")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_at")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull(message = "Date is required")
    private Date orderAt;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
