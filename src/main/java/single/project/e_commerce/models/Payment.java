package single.project.e_commerce.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.PaymentMethod;

import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "price")
    @Min(value = 0, message = "price must be greater than 0")
    private double price;

    @Column(name = "payment_method")
    @NotNull(message = "payment method is required")
    @Enumerated(EnumType.STRING)
    @EnumPattern(name = "paymentMethod", regexp = "VNPAY|PAY_WHEN_RECEIVED")
    private PaymentMethod paymentMethod;

    @Column(name = "paid_at")
    @NotNull(message = "paid date is required")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date paidAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
