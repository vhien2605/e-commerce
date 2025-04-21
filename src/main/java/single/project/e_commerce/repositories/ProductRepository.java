package single.project.e_commerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Product;
import single.project.e_commerce.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

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

    @Override
    @EntityGraph(attributePaths = {
            "categories",
            "shop"
    })
    List<Product> findAll(Specification<Product> specification, Sort sort);

    // not fetch collection because they can cause fetching in memory problem
    @Override
    @EntityGraph(attributePaths = {
            "shop"
    })
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);


    /**
     * the query fetching after pagination
     *
     * @param id id product
     * @return {@code List<Product>}
     */
    @EntityGraph(attributePaths = {
            "categories",
            "shop"
    })
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    public List<Product> findAllProductsWithId(@Param("ids") List<Long> id);


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
