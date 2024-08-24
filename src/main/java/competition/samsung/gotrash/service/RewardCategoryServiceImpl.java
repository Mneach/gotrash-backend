package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.RewardCategory;
import competition.samsung.gotrash.repository.RewardCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RewardCategoryServiceImpl implements RewardCategoryService {
    private RewardCategoryRepository rewardCategoryRepository;

    @Override
    public List<RewardCategory> findAll() {
        return rewardCategoryRepository.findAll();
    }

    @Override
    public Optional<RewardCategory> findById(String id) {
        return rewardCategoryRepository.findById(id);
    }

    @Override
    public RewardCategory save(RewardCategory rewardCategory) {
        return rewardCategoryRepository.save(rewardCategory);
    }

    @Override
    public String delete(String id) {
        rewardCategoryRepository.deleteById(id);
        return "Successfully deleted reward category with id: " + id;
    }
}
