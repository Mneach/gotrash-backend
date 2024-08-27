package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.dto.TrashBinDTO;
import competition.samsung.gotrash.entity.TrashBin;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.S3Service;
import competition.samsung.gotrash.service.TrashBinServiceImpl;
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
public class TrashBinController {

    private final TrashBinServiceImpl trashBinService;
    private final S3Service s3Service;

    @GetMapping("/trashbins")
    public StandardResponse<List<TrashBin>> getAllTrashBins() {
        List<TrashBin> trashBins = trashBinService.findAll();

        try{
            for(TrashBin trashBin : trashBins){
                if(!Objects.equals(trashBin.getImageName(), "")){
                    String presignUrl = s3Service.getPresignUrl(trashBin.getImageName());
                    trashBin.setImageUrl(presignUrl);
                }
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash bins", trashBins);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/trashbin/{id}")
    public StandardResponse<TrashBin> getTrashBinById(@PathVariable("id") String id) {
        Optional<TrashBin> data = trashBinService.findById(id);
        if (data.isEmpty()) {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }

        try{
            TrashBin trashBin = data.get();

            if(!Objects.equals(trashBin.getImageName(), "")){
                String presignUrl = s3Service.getPresignUrl(trashBin.getImageName());
                trashBin.setImageUrl(presignUrl);
            }

            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash bin", trashBin);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }


    }

    @PostMapping(value = "/trashbin/add", consumes = {"multipart/form-data"})
    public StandardResponse<TrashBin> createTrashBin(@ModelAttribute TrashBinDTO trashBinDTO) {

        TrashBin trashBin = new TrashBin();
        trashBin.setId(UUID.randomUUID().toString());
        trashBin.setName(trashBinDTO.getName());
        trashBin.setLatitude(trashBinDTO.getLatitude());
        trashBin.setLongitude(trashBinDTO.getLongitude());
        trashBin.setAddress(trashBinDTO.getAddress());

        try {
            String imageUrl = "";
            if(!trashBinDTO.getFile().isEmpty()){
                imageUrl = s3Service.uploadFileAndGetUrl(trashBinDTO.getFile(), ServiceName.TRASHBIN, trashBin.getId());
                trashBin.setImageName(trashBinDTO.getFile().getOriginalFilename());
            }else{
                trashBin.setImageName("");
            }

            TrashBin savedTrashBin = trashBinService.save(trashBin);
            savedTrashBin.setImageUrl(imageUrl);
            return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created trash bin", savedTrashBin);
        } catch (Exception e) {
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PatchMapping(value = "/trashbin/update/{id}", consumes = {"multipart/form-data"})
    public StandardResponse<TrashBin> updateTrashBin(@PathVariable("id") String id, @ModelAttribute TrashBinDTO trashBinDTO) {
        Optional<TrashBin> existingTrashBinOptional = trashBinService.findById(id);

        if (existingTrashBinOptional.isPresent()) {
            TrashBin existingTrashBin = existingTrashBinOptional.get();
            existingTrashBin.setName(trashBinDTO.getName());
            existingTrashBin.setLatitude(trashBinDTO.getLatitude());
            existingTrashBin.setLongitude(trashBinDTO.getLongitude());
            existingTrashBin.setAddress(trashBinDTO.getAddress());

            try {
                String imageUrl = "";
                if(!trashBinDTO.getFile().isEmpty()){
                    imageUrl = s3Service.uploadFileAndGetUrl(trashBinDTO.getFile(), ServiceName.TRASHBIN, trashBinDTO.getId().toString());
                    existingTrashBin.setImageName(trashBinDTO.getFile().getOriginalFilename());
                }else{
                    existingTrashBin.setImageUrl("");
                }

                TrashBin updatedTrashBin = trashBinService.save(existingTrashBin);
                updatedTrashBin.setImageUrl(imageUrl);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated trash bin", updatedTrashBin);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }
    }

    @DeleteMapping("/trashbin/delete/{id}")
    public StandardResponse<Void> deleteTrashBin(@PathVariable("id") String id) {
        Optional<TrashBin> existingTrashBin = trashBinService.findById(id);
        if (existingTrashBin.isPresent()) {

            TrashBin trashBin = existingTrashBin.get();
            trashBinService.delete(trashBin.getId());

            try {
                if (!Objects.equals(trashBin.getImageName(), "")) {
                    String objectKey = ServiceName.TRASHBIN + "/" + trashBin.getId() + "/";
                    s3Service.deleteFolder(objectKey);
                }

                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted trash bin with id: " + id, null);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }

        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }
    }
}
