package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.models.CartDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @EntityGraph(attributePaths = {
            "product",
            "cart"
    })
    @Query("SELECT cd FROM CartDetail cd INNER JOIN cd.product p WHERE p.id=:id")
    public Optional<CartDetail> findCartDetailByProductId(@Param("id") Long id);


    @EntityGraph(attributePaths = {
            "product",
            "cart"
    })
    @Query("SELECT cd FROM CartDetail cd INNER JOIN cd.cart c WHERE c.id=:id")
    public List<CartDetail> getAllCartDetailsByCartId(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetail cd WHERE cd.id IN :ids")
    public int deleteByIdsInCart(@Param("ids") List<Long> ids);
}
