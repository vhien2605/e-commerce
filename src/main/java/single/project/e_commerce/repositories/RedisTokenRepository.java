package single.project.e_commerce.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import single.project.e_commerce.models.RedisToken;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {

}
