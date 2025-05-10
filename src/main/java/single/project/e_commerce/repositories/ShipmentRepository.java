package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Shipment;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @EntityGraph(attributePaths = {
            "orderDetail",
            "orderDetail.shipment"
    })
    @Query("SELECT s FROM Shipment s INNER JOIN s.shop sp INNER JOIN sp.user u WHERE u.username=:username")
    List<Shipment> getAllShipmentFromShopOwnerUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {
            "orderDetail",
            "orderDetail.shipment"
    })
    @Query("SELECT s FROM Shipment s INNER JOIN s.user u WHERE u.username=:username")
    List<Shipment> getAllShipmentFromUserUsername(@Param("username") String username);


    @EntityGraph(attributePaths = {
            "orderDetail",
            "orderDetail.shipment"
    })
    @Query("SELECT s FROM Shipment s WHERE s.id=:id")
    Optional<Shipment> findShipmentById(@Param("id") Long id);
}
