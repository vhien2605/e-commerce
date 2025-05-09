package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT od,s.id FROM OrderDetail od INNER JOIN od.order o INNER JOIN od.product p INNER JOIN p.shop s WHERE o.id IN :orderIds")
    List<Object[]> findAllByOrderIdsWithShop(@Param("orderIds") List<Long> orderIds);
}
