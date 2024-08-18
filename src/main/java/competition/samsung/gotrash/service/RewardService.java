package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.Trash;

import java.util.List;

public interface RewardService {
    List<Reward> findAll();
    Reward findById(String id);
    Reward save(Reward reward);
    String delete(String id);
}
