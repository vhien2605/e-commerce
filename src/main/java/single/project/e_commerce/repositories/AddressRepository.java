package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a WHERE LOWER(a.name) = LOWER(:name) " +
            "AND LOWER(a.city) = LOWER(:city) " +
            "AND LOWER(a.country) = LOWER(:country)")
    Optional<Address> getAddressByLocation(@Param("name") String name,
                                           @Param("city") String city,
                                           @Param("country") String country);


    @Query("SELECT DISTINCT a FROM Address a " +
            "JOIN FETCH a.users u " +
            "JOIN FETCH u.roles " +
            "WHERE LOWER(a.name) = LOWER(:name) " +
            "AND LOWER(a.city) = LOWER(:city) " +
            "AND LOWER(a.country) = LOWER(:country)")
    Optional<Address> getAddressWithUsers(@Param("name") String name,
                                          @Param("city") String city,
                                          @Param("country") String country);
}
