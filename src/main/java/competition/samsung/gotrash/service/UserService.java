package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StreakResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    String delete(Integer id);
    void calculateWeeklyStreak(User user);
    List<StreakResponse> getWeeklyStreakData(User user);
}
