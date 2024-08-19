package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.TrashBin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrashBinRepository extends MongoRepository<TrashBin, String> {
}
