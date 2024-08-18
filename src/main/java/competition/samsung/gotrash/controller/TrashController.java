package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.service.TrashService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trash")
@AllArgsConstructor
public class TrashController {
    private TrashService trashService;
}
