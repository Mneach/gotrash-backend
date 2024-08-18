package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
}
