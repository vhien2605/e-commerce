package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
