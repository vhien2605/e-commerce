package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
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
            "address",
            "cart",
            "shop"
    })
    @Query("SELECT u FROM User u")
    public List<User> findAllUsersWithAllReferences();
}
