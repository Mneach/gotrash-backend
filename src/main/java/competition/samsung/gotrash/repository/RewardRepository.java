package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RewardRepository extends MongoRepository<Reward, Integer> {
}
