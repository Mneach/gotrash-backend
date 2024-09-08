package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.dto.RewardDTO;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.RewardCategory;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.RewardCategoryService;
import competition.samsung.gotrash.service.implement.RewardServiceImpl;
import competition.samsung.gotrash.service.S3Service;
import competition.samsung.gotrash.utils.S3BucketUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        List<Reward> rewards = rewardService.findAll();

        try{
            for(Reward reward : rewards){
                if(!Objects.equals(reward.getImageName(), "")){
                    String objectKeys = S3BucketUtil.createObjectKey(ServiceName.REWARD, reward.getId(), reward.getImageName());
                    String presignUrl = s3Service.getPresignUrl(objectKeys);
                    reward.setImageUrl(presignUrl);
                }
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved rewards" , rewards);
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/reward/{id}")
    public StandardResponse<Reward> getRewardById(@PathVariable("id") String id) {
        Optional<Reward> data = rewardService.findById(id);
        if (data.isEmpty()) {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
        }

        try{
            Reward reward = data.get();

            if(!Objects.equals(reward.getImageName(), "")){
                String objectKeys = S3BucketUtil.createObjectKey(ServiceName.REWARD, reward.getId(), reward.getImageName());
                String presignUrl = s3Service.getPresignUrl(objectKeys);
                reward.setImageUrl(presignUrl);
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved reward" , reward);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
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

                String imageUrl = "";
                if(rewardDTO.getFile() != null && !rewardDTO.getFile().isEmpty()){
                    imageUrl = s3Service.uploadFileAndGetUrl(rewardDTO.getFile(), ServiceName.REWARD, reward.getId());
                    reward.setImageName(rewardDTO.getFile().getOriginalFilename());
                }else if(rewardDTO.getFile() != null && rewardDTO.getFile().isEmpty()){
                    reward.setImageName("");
                }

                Reward savedReward = rewardService.save(reward);
                savedReward.setImageUrl(imageUrl);
                return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created reward", savedReward);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
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
                    String imageUrl = "";
                    if(rewardDTO.getFile() != null && !rewardDTO.getFile().isEmpty()){
                        imageUrl = s3Service.uploadFileAndGetUrl(rewardDTO.getFile(), ServiceName.REWARD, existingReward.getId());
                        existingReward.setImageName(rewardDTO.getFile().getOriginalFilename());
                    }else if(rewardDTO.getFile() != null && rewardDTO.getFile().isEmpty()){
                        existingReward.setImageName("");
                    }else if(rewardDTO.getFile() == null && !Objects.equals(existingReward.getImageName(), "")){
                        String objectKeys = S3BucketUtil.createObjectKey(ServiceName.USER, existingReward.getId(), existingReward.getImageName());
                        imageUrl = s3Service.getPresignUrl(objectKeys);
                    }

                    Reward updatedReward = rewardService.save(existingReward);
                    updatedReward.setImageUrl(imageUrl);
                    return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated reward", updatedReward);
                } catch (Exception e) {
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
            Reward reward = existingReward.get();
            rewardService.delete(reward.getId());

            try{
                if (!Objects.equals(reward.getImageName(), "")) {
                    String objectKey = ServiceName.REWARD_CATEGORY + "/" + reward.getId() + "/";
                    s3Service.deleteFolder(objectKey);
                }
                return new StandardResponse<>(HttpStatus.OK.value(),"Successfully deleted reward" + id, null);
            }catch (Exception e){
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }

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
