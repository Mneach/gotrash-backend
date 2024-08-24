package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.RewardCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RewardCategoryRepository extends MongoRepository<RewardCategory, String> {
}