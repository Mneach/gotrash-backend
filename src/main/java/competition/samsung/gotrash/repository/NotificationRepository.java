package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(Integer userId);
}
