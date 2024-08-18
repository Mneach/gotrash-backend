package competition.samsung.gotrash.repository;

import org.apache.catalina.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, Integer> {
}
