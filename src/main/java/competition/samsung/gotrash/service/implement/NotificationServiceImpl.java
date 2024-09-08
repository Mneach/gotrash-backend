package competition.samsung.gotrash.service.implement;

import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.NotificationRepository;
import competition.samsung.gotrash.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(String id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findByUserId(Integer id) {
        return notificationRepository.findByUserId(id);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public String delete(String id) {
        notificationRepository.deleteById(id);
        return "Successfully delete notification with id : " + id;
    }
}
