package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.RewardServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RewardController {

    private final RewardServiceImpl rewardService;
//    private final S3Service s3Service;

    @GetMapping("/rewards")
    public StandardResponse<List<Reward>> getAllRewards() {
        List<Reward> rewardsList = rewardService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved rewards" , rewardsList);
    }

    @GetMapping("/reward/{id}")
    public StandardResponse<Reward> getRewardById(@PathVariable("id") String id) {
        Optional<Reward> reward = rewardService.findById(id);
        if (reward.isPresent()) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved reward" ,reward.get());
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
        }
    }

    @PostMapping("/reward/add")
    public StandardResponse<Reward> createReward(@RequestBody Reward reward) {
        reward.setId(UUID.randomUUID().toString());
        Reward savedReward = rewardService.save(reward);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created reward", savedReward);
    }

    @PatchMapping("/reward/update/{id}")
    public StandardResponse<Reward> updateReward(@PathVariable("id") String id, @RequestBody Reward reward) {
        Optional<Reward> existingRewardOptional = rewardService.findById(id);

        if (existingRewardOptional.isPresent()) {
            Reward existingReward = existingRewardOptional.get();

            existingReward.setName(reward.getName());
            existingReward.setCoin(reward.getCoin());
            existingReward.setUpdatedAt(LocalDateTime.now());

            Reward updatedReward = rewardService.save(existingReward);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated reward", updatedReward);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
        }
    }

    @DeleteMapping("/reward/delete/{id}")
    public StandardResponse<Void> deleteReward(@PathVariable("id") String id) {
        Optional<Reward> existingReward = rewardService.findById(id);
        if (existingReward.isPresent()) {
            rewardService.delete(id);
            return new StandardResponse<>(HttpStatus.OK.value(),"Successfully deleted reward with id: " + id, null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
        }
    }

//    @PostMapping("/upload")
//    public StandardResponse<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
//        return new StandardResponse<>(HttpStatus.OK.value(), "Success", s3Service.uploadFile(file));
//    }
}
