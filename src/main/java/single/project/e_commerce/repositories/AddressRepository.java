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
import single.project.e_commerce.models.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    @Query("SELECT a FROM Address a WHERE LOWER(a.name) = LOWER(:name) " +
            "AND LOWER(a.city) = LOWER(:city) " +
            "AND LOWER(a.country) = LOWER(:country)")
    Optional<Address> getAddressByLocation(@Param("name") String name,
                                           @Param("city") String city,
                                           @Param("country") String country);


    @EntityGraph(attributePaths = {
            "users",
            "users.roles",
    })
    @Query("SELECT a FROM Address a WHERE LOWER(a.name) = LOWER(:name) AND LOWER(a.city) = LOWER(:city) AND LOWER(a.country) = LOWER(:country)")
    Optional<Address> getAddressWithUsersAndRoles(@Param("name") String name,
                                                  @Param("city") String city,
                                                  @Param("country") String country);


    @Override
    public List<Address> findAll();


    public List<Address> findAll(Specification<Address> specification, Sort sort);

    public Page<Address> findAll(Specification<Address> specification, Pageable pageable);
}
