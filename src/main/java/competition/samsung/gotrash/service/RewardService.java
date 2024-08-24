package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Reward;

import java.util.List;
import java.util.Optional;

public interface RewardService {
    List<Reward> findAll();
    Optional<Reward> findById(String id);
    Reward save(Reward reward);
    String delete(String id);
    List<Reward> findByRewardCategoryId(String rewardCategoryId);
}
