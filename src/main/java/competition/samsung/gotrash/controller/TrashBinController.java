package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.entity.TrashBin;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.TrashBinServiceImpl;
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
public class TrashBinController {

    private final TrashBinServiceImpl trashBinService;

    @GetMapping("/trashbins")
    public StandardResponse<List<TrashBin>> getAllTrashBins() {
        List<TrashBin> trashBins = trashBinService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash bins", trashBins);
    }

    @GetMapping("/trashbin/{id}")
    public StandardResponse<TrashBin> getTrashBinById(@PathVariable("id") String id) {
        Optional<TrashBin> trashBin = trashBinService.findById(id);
        if (trashBin.isPresent()) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash bin", trashBin.get());
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }
    }

    @PostMapping("/trashbin/add")
    public StandardResponse<TrashBin> createTrashBin(@RequestBody TrashBin trashBin) {
        trashBin.setId(UUID.randomUUID().toString());
        TrashBin savedTrashBin = trashBinService.save(trashBin);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created trash bin", savedTrashBin);
    }

    @PatchMapping("/trashbin/update/{id}")
    public StandardResponse<TrashBin> updateTrashBin(@PathVariable("id") String id, @RequestBody TrashBin trashBin) {
        Optional<TrashBin> existingTrashBinOptional = trashBinService.findById(id);

        if (existingTrashBinOptional.isPresent()) {
            TrashBin existingTrashBin = existingTrashBinOptional.get();
            existingTrashBin.setName(trashBin.getName());
            existingTrashBin.setLatitude(trashBin.getLatitude());
            existingTrashBin.setLongitude(trashBin.getLongitude());
            existingTrashBin.setUpdatedAt(LocalDateTime.now());

            // Save the updated TrashBin
            TrashBin updatedTrashBin = trashBinService.save(existingTrashBin);

            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated trash bin", updatedTrashBin);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }
    }

    @DeleteMapping("/trashbin/delete/{id}")
    public StandardResponse<Void> deleteTrashBin(@PathVariable("id") String id) {
        Optional<TrashBin> existingTrashBin = trashBinService.findById(id);
        if (existingTrashBin.isPresent()) {
            trashBinService.delete(id);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted trash bin with id: " + id, null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Bin Not Found", null);
        }
    }
}
