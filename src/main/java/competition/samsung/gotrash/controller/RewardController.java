package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.service.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reward")
@AllArgsConstructor
public class RewardController {
    private RewardService rewardService;
}
