package single.project.e_commerce.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import single.project.e_commerce.utils.annotations.EnumPattern;
import single.project.e_commerce.utils.enums.ShippingStatus;


import java.util.Date;


@Entity
@Table(name = "shipments")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipment extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank(message = "tracking id must not be blank")
    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "receiver_address")
    @NotBlank(message = "receiver address must be required")
    private String receiverAddress;

    @Column(name = "receiver_number")
    @NotBlank(message = "receiver number must be required")
    private String receiverNumber;

    @NotBlank(message = "shipping status must not be blank")
    @Column(name = "shipping_status")
    @EnumPattern(name = "shippingStatus", regexp = "SHIPPED|SHIPPING")
    @Enumerated(EnumType.STRING)
    private ShippingStatus shippingStatus;

    @Column(name = "delivered_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Date is required")
    private Date deliveredAt;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
