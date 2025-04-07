package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.roles r " +
            "JOIN FETCH r.permissions " +
            "JOIN FETCH u.address " +
            "WHERE u.username = :username")
    Optional<User> findUserWithRoleAndPermissionByUsername(@Param("username") String username);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN FETCH u.roles r " +
            "JOIN FETCH u.address a " +
            "JOIN FETCH r.permissions"
    )
    public List<User> findAllUsersWithAllReferences();
}
