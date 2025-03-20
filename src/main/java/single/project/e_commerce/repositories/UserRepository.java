package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles r " +
            "JOIN FETCH r.permissions WHERE u.id=:userId")
    public Optional<User> findUserWithRoleAndPermission(@Param("userId") Long userId);

    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH " +
            "u.roles r JOIN FETCH r.permissions WHERE u.username=:username")
    public Optional<User> findUserWithRoleAndPermissionByUsername(@Param("username") String username);

    public boolean existsByUsername(String username);
}
