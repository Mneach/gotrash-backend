package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
