package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Role;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findAllByNameIn(Collection<String> names);

    boolean existsByName(String name);


    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.name=:name")
    Optional<Role> findRoleWithPermissionsByName(@Param("name") String name);
}
