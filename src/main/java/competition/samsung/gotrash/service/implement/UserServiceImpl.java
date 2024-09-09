package competition.samsung.gotrash.service.implement;

import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.UserRepository;
import competition.samsung.gotrash.response.StreakResponse;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public String delete(Integer id) {
        userRepository.deleteById(id);
        return "Successfully delete user with id : " + id;
    }

    public void calculateWeeklyStreak(User user){
        LocalDateTime now = LocalDateTime.now();

        // Get the Monday of the current week at 00:00
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // Get the Sunday of the current week at 23:59
        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withHour(23).withMinute(59).withSecond(59).withNano(0);

        // Check if the user has trash input in the current week
        boolean hasTrashThisWeek = user.getTrashHistory().stream()
                .anyMatch(trash -> trash.getCreatedAt().isAfter(startOfWeek) && trash.getCreatedAt().isBefore(endOfWeek));

        // Handle streak update
        if (user.getLastStreakResetDate() == null || user.getLastStreakResetDate().isBefore(startOfWeek)) {
            if (hasTrashThisWeek) {
                user.setCurrentStreak(user.getCurrentStreak() + 1);
            }
            user.setLastStreakResetDate(endOfWeek); // Update last reset date
        }else if(user.getLastStreakResetDate().plusWeeks(1).isAfter(startOfWeek) && user.getLastStreakResetDate().plusWeeks(1).isBefore(endOfWeek)){
            // reset only if user doesn't have trash history one week before
            user.setCurrentStreak(0);
            user.setLastStreakResetDate(endOfWeek);  // Update last reset date
        }

        // Save the updated user data
        userRepository.save(user);
    }

    public List<StreakResponse> getWeeklyStreakData(User user) {
        List<StreakResponse> streakData = new ArrayList<>();
        LocalDateTime registrationDate = user.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        // Loop from user registration date until now
        LocalDateTime currentStartDate = registrationDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        while (currentStartDate.isBefore(now)) {
            LocalDateTime currentEndDate = currentStartDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            // Filter trash history for this week
            LocalDateTime finalCurrentStartDate = currentStartDate;
            List<Trash> weeklyTrashHistory = user.getTrashHistory().stream()
                    .filter(trash -> !trash.getCreatedAt().isBefore(finalCurrentStartDate) && !trash.getCreatedAt().isAfter(currentEndDate))
                    .collect(Collectors.toList());

            // Create a StreakResponse object for this week
            StreakResponse weekData = new StreakResponse(currentStartDate, currentEndDate, weeklyTrashHistory);

            // Add to the list
            streakData.add(weekData);

            // Move to the next week
            currentStartDate = currentStartDate.plusWeeks(1);
        }

        return streakData;
    }
}
