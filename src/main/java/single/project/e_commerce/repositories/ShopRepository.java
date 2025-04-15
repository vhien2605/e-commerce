package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
}
