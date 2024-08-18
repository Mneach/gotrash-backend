package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, Integer> {
}
