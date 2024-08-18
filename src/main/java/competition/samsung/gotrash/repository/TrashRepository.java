package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Trash;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrashRepository extends MongoRepository<Trash, String> {
}
