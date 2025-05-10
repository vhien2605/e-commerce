package single.project.e_commerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Order;
import single.project.e_commerce.utils.enums.OrderStatus;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {
            "payment",
            "orderDetails",
            "orderDetails.shipment",
            "orderDetails.product"
    })
    @Query("SELECT o FROM Order o INNER JOIN o.user u WHERE u.username=:username")
    public List<Order> findAllOrdersByUserUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {
            "payment",
            "orderDetails",
            "orderDetails.shipment",
            "orderDetails.product"
    })
    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    public List<Order> findAllByIdsNoPagination(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {
            "payment"
    })
    @Query("SELECT o FROM Order o INNER JOIN o.user u WHERE u.username=:username AND o.id=:id")
    public Optional<Order> findOrderByUserUsernameAndId(@Param("username") String username,
                                                        @Param("id") long id);


    @Override
    // just pageable , not fetching collection avoid pagination in memory
    @EntityGraph(attributePaths = {
            "payment"
    })
    public Page<Order> findAll(Specification<Order> specification, Pageable pageable);


    // fetch sub entities in the second query
    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.shipment",
            "orderDetails.product"
    })
    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    public List<Order> subPaginationOrder(@Param("ids") List<Long> ids);


    @EntityGraph(attributePaths = {
            "payment",
            "orderDetails",
            "orderDetails.shipment",
            "orderDetails.product"
    })
    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    public List<Order> findAllOrdersByIdsWithCollection(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {
            "payment"
    })
    @Query("SELECT o FROM Order o WHERE o.id IN :ids")
    public List<Order> findAllOrdersWithIdsNoCollectionFetching(@Param("ids") List<Long> ids);

    @Modifying
    @Query("UPDATE Order o SET o.status=:status WHERE o.id IN :ids AND o.status = 'UNPAID'")
    public void updateStatusOrderByOrderIds(@Param("ids") List<Long> ids, @Param("status") OrderStatus status);
}
