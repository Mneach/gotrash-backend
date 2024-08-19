package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.NotificationServiceImpl;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;
    private final UserService userService;

    @GetMapping("/notifications")
    public StandardResponse<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved notifications", notifications);
    }

    @GetMapping("/notification/{id}")
    public StandardResponse<Notification> getNotificationById(@PathVariable("id") String id) {
        Optional<Notification> notification = notificationService.findById(id);
        if (notification.isPresent()) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved notification", notification.get());
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Notification Not Found", null);
        }
    }

    @GetMapping("/notifications/user/{id}")
    public StandardResponse<List<Notification>> getNotificationsByUser(@PathVariable int id) {
        Optional<User> user = userService.findById(id);

        if(user.isPresent()){
            List<Notification> notifications = notificationService.findByUser(user.get());
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved notifications for user", notifications);
        }else{
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

    }

    @PostMapping("/notification/add")
    public StandardResponse<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationService.save(notification);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created notification", savedNotification);
    }

    @PutMapping("/notification/update/{id}")
    public StandardResponse<Notification> updateNotification(@PathVariable("id") String id, @RequestBody Notification notification) {
        Optional<Notification> existingNotificationOptional = notificationService.findById(id);

        if (existingNotificationOptional.isPresent()) {
            Notification existingNotification = existingNotificationOptional.get();

            existingNotification.setNotificationTitle(notification.getNotificationTitle());
            existingNotification.setNotificationDescription(notification.getNotificationDescription());
            existingNotification.setUser(notification.getUser());
            existingNotification.setUpdatedAt(LocalDateTime.now());

            Notification updatedNotification = notificationService.save(existingNotification);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated notification", updatedNotification);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Notification Not Found", null);
        }
    }

    @DeleteMapping("/notification/delete/{id}")
    public StandardResponse<Void> deleteNotification(@PathVariable("id") String id) {
        Optional<Notification> existingNotification = notificationService.findById(id);
        if (existingNotification.isPresent()) {
            notificationService.delete(id);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted notification with id: " + id, null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Notification Not Found", null);
        }
    }
}
