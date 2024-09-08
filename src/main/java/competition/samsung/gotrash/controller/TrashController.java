package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.S3Service;
import competition.samsung.gotrash.service.generator.SequenceGeneratorService;
import competition.samsung.gotrash.service.TrashService;
import competition.samsung.gotrash.service.generator.TrashSequenceGeneratorService;
import competition.samsung.gotrash.utils.S3BucketUtil;
import competition.samsung.gotrash.utils.TrashUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static competition.samsung.gotrash.entity.User.SEQUENCE_NAME;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TrashController {

    private TrashService trashService;
    private TrashSequenceGeneratorService trashSequenceGeneratorService;
    private S3Service s3Service;

    @GetMapping("/trashes")
    public StandardResponse<List<Trash>> getAllTrashes() {
        try{
            List<Trash> trashList = trashService.findAll();
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trashes", trashList);
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/trash/{id}")
    public StandardResponse<Trash> getTrashById(@PathVariable("id") Integer id) {
        try{
            Optional<Trash> OptionalTrash = trashService.findById(id);
            if (OptionalTrash.isPresent()) {
                try{
                    Trash trash = OptionalTrash.get();
                    return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash", trash);
                }catch (Exception e){
                    return new StandardResponse<>(HttpStatus.OK.value(), e.getMessage(), null);
                }
            } else {
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
            }
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/trash/add")
    public StandardResponse<Trash> createTrash(@RequestBody Trash trash) {
        Integer id = trashSequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME);
        trash.setId(id);

        trash.setCoin(TrashUtil.GetTrashCoin(trash));
        trash.setRating(TrashUtil.GetTrashRating(trash));

        Trash savedTrash = trashService.save(trash);

        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created trash", savedTrash);
    }

    @PatchMapping("/trash/update/{id}")
    public StandardResponse<Trash> updateTrash(@PathVariable("id") Integer id, @RequestBody Trash trash) {
        try{
            Optional<Trash> trashOptional = trashService.findById(id);
            if (trashOptional.isPresent()) {
                Trash existingTrash = trashOptional.get();
                existingTrash.setCoin(trash.getCoin());
                existingTrash.setCategory(trash.getCategory());
                existingTrash.setRating(trash.getRating());
                existingTrash.setDescription(trash.getDescription());
                existingTrash.setUpdatedAt(LocalDateTime.now());

                Trash updatedTrash = trashService.save(existingTrash);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated trash", updatedTrash);
            } else {
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
            }
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @DeleteMapping("/trash/delete/{id}")
    public StandardResponse<Void> deleteTrash(@PathVariable("id") Integer id) {
        try{
            Optional<Trash> existingTrash = trashService.findById(id);
            if (existingTrash.isPresent()) {
                trashService.delete(id);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted trash with id: " + id, null);
            } else {
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
            }
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

}
