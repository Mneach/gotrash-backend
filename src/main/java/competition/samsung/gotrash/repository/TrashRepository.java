package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.Trash;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrashRepository extends MongoRepository<Trash, Integer> {
    Optional<Trash> findFirstByCategory(String category);

    @Query(sort = "{ 'id': -1 }")
    Optional<Trash> findLastTrashById();
}
