package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.payment"
    })
    @Query("SELECT o FROM Order o INNER JOIN o.user u WHERE u.username=:username")
    public List<Order> findAllOrdersByUserUsername(@Param("username") String username);
}
