package competition.samsung.gotrash.rest;

import competition.samsung.gotrash.service.GroupService;
import competition.samsung.gotrash.service.GroupServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
@AllArgsConstructor
public class GroupController {
    private GroupService groupService;
}
