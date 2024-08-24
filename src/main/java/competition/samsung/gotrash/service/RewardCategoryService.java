package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.RewardCategory;

import java.util.List;
import java.util.Optional;

public interface RewardCategoryService {
    public List<RewardCategory> findAll();

    public Optional<RewardCategory> findById(String id);

    public RewardCategory save(RewardCategory rewardCategory);

    public String delete(String id);
}
