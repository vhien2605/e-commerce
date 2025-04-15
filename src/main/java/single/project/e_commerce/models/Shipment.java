package single.project.e_commerce.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


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

    @NotBlank(message = "shipping vehicle must not be blank")
    @Column(name = "shipping_vehicle")
    private String shippingVehicle;

    @NotBlank(message = "shipping status must not be blank")
    @Column(name = "shipping_status")
    private String shippingStatus;


    @Column(name = "delivered_at")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Date is required")
    private Date deliveredAt;

    @OneToOne(mappedBy = "shipment")
    private Order order;
}
