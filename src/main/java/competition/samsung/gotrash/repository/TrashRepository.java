package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.Trash;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrashRepository extends MongoRepository<Trash, String> {
    Optional<Trash> findFirstByCategory(String category);
}
