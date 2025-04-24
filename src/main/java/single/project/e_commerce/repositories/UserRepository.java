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
import single.project.e_commerce.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @EntityGraph(attributePaths = {
            "cart",
            "address",
            "shop",
            "roles"
    })
    public Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions",
            "address",
            "cart",
            "shop"
    })
    @Query("SELECT u From User u WHERE u.username=:username")
    Optional<User> findUserWithRoleAndPermissionByUsername(@Param("username") String username);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);


    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions",
            "shop",
            "cart"
    })
    @Query("SELECT u FROM User u")
    public List<User> findAllUsersWithAllReferences();


    @EntityGraph(attributePaths = {
            "roles",
            "roles.permissions",
            "address",
            "cart",
            "shop"
    })
    @Override
    public List<User> findAll(Specification<User> specification, Sort sort);

    // to avoid pageable in memory problem
    // not fetch collection
    @EntityGraph(attributePaths = {
            "address",
            "cart",
            "shop"
    })
    @Override
    public Page<User> findAll(Specification<User> specification, Pageable pageable);

    @EntityGraph(attributePaths = {
            "address",
            "cart",
            "shop",
            "roles",
            "roles.permissions"
    })
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    public List<User> findAllUsersWithId(@Param("ids") List<Long> id);
}
