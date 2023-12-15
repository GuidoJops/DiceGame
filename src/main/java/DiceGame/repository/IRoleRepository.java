package DiceGame.repository;

import DiceGame.model.domain.ERole;
import DiceGame.model.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends  MongoRepository <Role, Long>{

    Optional<Role> findByType(ERole type);

}
