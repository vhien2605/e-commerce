package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Shop;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Override
    @EntityGraph(attributePaths = {
            "user",
            "user.address",
            "user.cart",
            "user.shop"
    })
    Optional<Shop> findById(Long id);


    @Override
    @EntityGraph(attributePaths = {
            "user",
            "user.address",
            "user.cart",
            "user.shop"
    })
    List<Shop> findAll();

    @EntityGraph(attributePaths = {
            "user",
            "user.address",
            "user.cart",
            "user.shop"
    })
    @Query("SELECT s FROM Shop s INNER JOIN s.user u WHERE u.username=:username")
    Optional<Shop> findByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {
            "user",
            "user.address",
            "user.cart",
            "user.shop"
    })
    @Query("SELECT s FROM Shop s INNER JOIN s.products p WHERE p.id=:id")
    Optional<Shop> findByProductId(@Param("id") Long id);
}
