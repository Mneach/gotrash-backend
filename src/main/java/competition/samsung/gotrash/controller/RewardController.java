package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.dto.RewardDTO;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.RewardCategory;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.RewardCategoryService;
import competition.samsung.gotrash.service.RewardServiceImpl;
import competition.samsung.gotrash.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RewardController {
    private final RewardCategoryService rewardCategoryService;
    private final RewardServiceImpl rewardService;
    private final S3Service s3Service;

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

    @PostMapping(value = "/reward/add", consumes = {"multipart/form-data"})
    public StandardResponse<Reward> createReward(@ModelAttribute RewardDTO rewardDTO) {
        Reward reward = new Reward();

        reward.setId(UUID.randomUUID().toString());
        reward.setName(rewardDTO.getName());
        reward.setCoin(rewardDTO.getCoin());
        reward.setDescription(rewardDTO.getDescription());
        reward.setRewardCategoryId(rewardDTO.getRewardCategoryId());

        Optional<RewardCategory> existingRewardCategoryOptional = rewardCategoryService.findById(rewardDTO.getRewardCategoryId());

        if(existingRewardCategoryOptional.isPresent()){
            try {
                if(!rewardDTO.getFile().isEmpty()){
                    String imageUrl = s3Service.uploadFileAndGetUrl(rewardDTO.getFile());
                    reward.setImageUrl(imageUrl);
                }

                Reward savedReward = rewardService.save(reward);
                return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created reward", savedReward);
            } catch (Exception e) {
                // Handle the exception and return an error response
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload file", null);
            }
        }else{
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Category Found", null);
        }
    }

    @PatchMapping(value = "/reward/update/{id}", consumes = {"multipart/form-data"})
    public StandardResponse<Reward> updateReward(@PathVariable("id") String id, @ModelAttribute RewardDTO rewardDTO) {
        Optional<Reward> existingRewardOptional = rewardService.findById(id);

        if (existingRewardOptional.isPresent()) {
            Reward existingReward = existingRewardOptional.get();

            Optional<RewardCategory> existingRewardCategoryOptional = rewardCategoryService.findById(existingReward.getRewardCategoryId());

            if(existingRewardCategoryOptional.isPresent()){
                existingReward.setName(rewardDTO.getName());
                existingReward.setCoin(rewardDTO.getCoin());
                existingReward.setUpdatedAt(LocalDateTime.now());
                existingReward.setDescription(rewardDTO.getDescription());

                try {
                    if(!rewardDTO.getFile().isEmpty()){
                        String imageUrl = s3Service.uploadFileAndGetUrl(rewardDTO.getFile());
                        existingReward.setImageUrl(imageUrl);
                    }

                    Reward updatedReward = rewardService.save(existingReward);
                    return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated reward", updatedReward);
                } catch (Exception e) {
                    // Handle the exception and return an error response
                    return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload file", null);
                }
            }else{
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Category Found", null);
            }

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

    @GetMapping("/rewards/category/{id}")
    public StandardResponse<List<Reward>> getRewardsByCategory(@PathVariable("id") String id) {
        List<Reward> rewards = rewardService.findByRewardCategoryId(id);
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved rewards category", rewards);
    }
}
