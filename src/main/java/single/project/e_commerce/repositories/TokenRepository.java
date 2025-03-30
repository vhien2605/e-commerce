package single.project.e_commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

}
