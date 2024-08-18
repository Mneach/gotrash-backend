package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
public class NotificationController {

    private NotificationService notificationService;
}
