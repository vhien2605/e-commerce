package single.project.e_commerce.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r INNER JOIN r.user u WHERE u.username=:username")
    public List<Review> getByUsername(@Param("username") String username);
}
