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
    @Query("SELECT u FROM User u JOIN FETCH u.roles r " +
            "JOIN FETCH r.permissions WHERE u.id=:userId")
    public Optional<User> findUserWithRoleAndPermission(@Param("userId") Long userId);

    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u " +
            "JOIN FETCH u.roles r " +
            "JOIN FETCH r.permissions " +
            "JOIN FETCH u.address " +
            "WHERE u.username = :username")
    Optional<User> findUserWithRoleAndPermissionByUsername(@Param("username") String username);


    public boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles r")
    public List<User> findAll();
    
}
