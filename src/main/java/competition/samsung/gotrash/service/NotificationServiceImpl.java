package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification findById(String id) {
        return notificationRepository.findById(id).get();
    }

    @Override
    public List<Notification> findByUser(User user){
        return notificationRepository.findByUser(user);
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
