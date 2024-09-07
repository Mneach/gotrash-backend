package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group, String> {
    @Query("{ 'members.id' : ?0 }")
    List<Group> findGroupsByUserId(Integer userId);
}
