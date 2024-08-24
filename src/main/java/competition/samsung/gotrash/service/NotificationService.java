package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> findAll();
    Optional<Notification> findById(String id);
    List<Notification>  findByUserId(Integer id);
    Notification save(Notification notification);
    String delete(String id);
}
