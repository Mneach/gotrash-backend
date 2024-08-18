package competition.samsung.gotrash.repository;

import competition.samsung.gotrash.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Integer> {
}
