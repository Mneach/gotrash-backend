package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.repository.RewardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RewardServiceImpl implements RewardService{

    private RewardRepository rewardRepository;

    @Override
    public List<Reward> findAll() {
        return rewardRepository.findAll();
    }

    @Override
    public Optional<Reward> findById(String id) {
        return rewardRepository.findById(id);
    }

    @Override
    public Reward save(Reward reward) {
        return rewardRepository.save(reward);
    }

    @Override
    public String delete(String id) {
        rewardRepository.deleteById(id);
        return "Successfully delete reward with id : " + id;
    }
}
