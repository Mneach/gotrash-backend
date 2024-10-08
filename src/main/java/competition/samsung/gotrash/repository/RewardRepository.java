package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Reward;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RewardRepository extends MongoRepository<Reward, String> {
    List<Reward> findByRewardCategoryId(String rewardCategoryId);
}
