package single.project.e_commerce.repositories;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {
            "user.cart",
            "user.shop"
    })
    @Query("SELECT c FROM Cart c INNER JOIN c.user u WHERE u.username=:username")
    Optional<Cart> getCartByUserUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {
            "cartDetails",
            "cartDetails.product"
    })
    @Query("SELECT c FROM Cart c INNER JOIN c.user u WHERE u.username=:username")
    Optional<Cart> getCartByUserUsernameWithCartItems(@Param("username") String username);
}
