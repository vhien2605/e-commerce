package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "categories",
            "shop"
    })
    public Optional<Product> findById(Long id);


    @Override
    @EntityGraph(attributePaths = {
            "categories",
            "shop"
    })
    List<Product> findAll();


    @EntityGraph(attributePaths = {
            "categories",
            "reviews",
            "shop"
    })
    @Query("SELECT p FROM Product p WHERE p.id=:id")
    public Optional<Product> findByIdWithReviews(@Param("id") Long id);


    @EntityGraph(attributePaths = {
            "categories",
            "shop"
    })
    @Query("SELECT p FROM Product p INNER JOIN p.shop s INNER JOIN s.user u WHERE u.username=:username")
    public List<Product> findProductsByUsername(@Param("username") String username);
}
