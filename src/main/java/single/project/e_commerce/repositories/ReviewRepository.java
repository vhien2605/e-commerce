package single.project.e_commerce.repositories;


import org.hibernate.query.spi.QueryOptionsAdapter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {
            "product",
            "user",
            "user.cart",
            "user.shop"
    })
    @Query("SELECT r FROM Review r INNER JOIN r.user u WHERE u.username=:username")
    public List<Review> getByUsername(@Param("username") String username);

    @Override
    @EntityGraph(attributePaths = {
            "product",
            "user",
            "user.cart",
            "user.shop"
    })
    Optional<Review> findById(Long id);
}
