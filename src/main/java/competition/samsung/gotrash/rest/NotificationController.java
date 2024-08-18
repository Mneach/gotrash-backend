package competition.samsung.gotrash.rest;

import competition.samsung.gotrash.service.NotificationService;
import competition.samsung.gotrash.service.NotificationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;
}
