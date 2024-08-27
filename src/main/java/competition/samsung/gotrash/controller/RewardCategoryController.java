package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.dto.RewardCategoryDTO;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.RewardCategory;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.RewardCategoryService;
import competition.samsung.gotrash.service.RewardService;
import competition.samsung.gotrash.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RewardCategoryController {

    private final RewardCategoryService rewardCategoryService;
    private final S3Service s3Service;

    @GetMapping("/reward-categories")
    public StandardResponse<List<RewardCategory>> getAllRewardCategories() {
        List<RewardCategory> rewardCategories = rewardCategoryService.findAll();

        try{
            for(RewardCategory rewardCategory : rewardCategories){
                if(!Objects.equals(rewardCategory.getImageName(), "")){
                    String presignUrl = s3Service.getPresignUrl(rewardCategory.getImageName());
                    rewardCategory.setImageUrl(presignUrl);
                }
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved reward categories", rewardCategories);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }


    }

    @GetMapping("/reward-category/{id}")
    public StandardResponse<RewardCategory> getRewardCategoryById(@PathVariable("id") String id) {
        Optional<RewardCategory> data = rewardCategoryService.findById(id);
        if (data.isEmpty()) {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward category not found", null);

        }

        try{
            RewardCategory rewardCategory = data.get();

            if(!Objects.equals(rewardCategory.getImageName(), "")){
                String presignUrl = s3Service.getPresignUrl(rewardCategory.getImageName());
                rewardCategory.setImageUrl(presignUrl);
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved reward category", rewardCategory);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }
    @PostMapping(value = "/reward-category/add", consumes = {"multipart/form-data"})
    public StandardResponse<RewardCategory> createRewardCategory(@ModelAttribute RewardCategoryDTO rewardCategoryDTO) {

        RewardCategory rewardCategory = new RewardCategory();
        rewardCategory.setId(UUID.randomUUID().toString());
        rewardCategory.setName(rewardCategoryDTO.getName());

        try {
            String imageUrl = "";
            if (!rewardCategoryDTO.getFile().isEmpty()) {
                imageUrl = s3Service.uploadFileAndGetUrl(rewardCategoryDTO.getFile(), "RewardCategory", rewardCategory.getId());
                rewardCategory.setImageName(rewardCategoryDTO.getFile().getOriginalFilename());
            }else{
                rewardCategory.setImageName("");
            }

            RewardCategory savedCategory = rewardCategoryService.save(rewardCategory);
            savedCategory.setImageUrl(imageUrl);
            return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created reward category", savedCategory);
        } catch (Exception e) {
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload file", null);
        }
    }

    @PatchMapping(value = "/reward-category/update/{id}", consumes = {"multipart/form-data"})
    public StandardResponse<RewardCategory> updateRewardCategory(@PathVariable("id") String id, @ModelAttribute RewardCategoryDTO rewardCategoryDTO) {
        Optional<RewardCategory> existingCategory = rewardCategoryService.findById(id);

        if (existingCategory.isPresent()) {
            RewardCategory rewardCategory = existingCategory.get();
            rewardCategory.setName(rewardCategoryDTO.getName());

            try {
                String imageUrl = "";
                if(!rewardCategoryDTO.getFile().isEmpty()){
                    imageUrl = s3Service.uploadFileAndGetUrl(rewardCategoryDTO.getFile(), ServiceName.REWARD, rewardCategory.getId());
                    rewardCategory.setImageName(rewardCategoryDTO.getFile().getOriginalFilename());
                }else{
                    rewardCategory.setImageName("");
                }

                RewardCategory updatedCategory = rewardCategoryService.save(rewardCategory);
                updatedCategory.setImageUrl(imageUrl);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated reward category", updatedCategory);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload file", null);
            }
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward category not found", null);
        }
    }

    @DeleteMapping("/reward-category/delete/{id}")
    public StandardResponse<Void> deleteRewardCategory(@PathVariable("id") String id) {
        Optional<RewardCategory> existingCategory = rewardCategoryService.findById(id);
        if (existingCategory.isPresent()) {
            RewardCategory rewardCategory = existingCategory.get();
            rewardCategoryService.delete(rewardCategory.getId());

            try{
                if(!Objects.equals(rewardCategory.getImageName(), "")){
                    String objectKey = ServiceName.REWARD + "/" + rewardCategory.getId() + "/";
                    s3Service.deleteFolder(objectKey);
                }
                rewardCategoryService.delete(id);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted reward category with id: " + id, null);
            }catch (Exception e){
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }
        }
        return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward category not found", null);
    }
}
