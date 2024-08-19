package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.TrashServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TrashController {

    private final TrashServiceImpl trashService;

    @GetMapping("/trashes")
    public StandardResponse<List<Trash>> getAllTrashes() {
        List<Trash> trashList = trashService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trashes", trashList);
    }

    @GetMapping("/trash/{id}")
    public StandardResponse<Trash> getTrashById(@PathVariable("id") String id) {
        Optional<Trash> trash = trashService.findById(id);
        if (trash.isPresent()) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved trash", trash.get());
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
        }
    }

    @PostMapping("/trash/add")
    public StandardResponse<Trash> createTrash(@RequestBody Trash trash) {
        Trash savedTrash = trashService.save(trash);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created trash", savedTrash);
    }

    @PutMapping("/trash/update/{id}")
    public StandardResponse<Trash> updateTrash(@PathVariable("id") String id, @RequestBody Trash trash) {
        Optional<Trash> trashOptional = trashService.findById(id);
        if (trashOptional.isPresent()) {
            Trash existingTrash = trashOptional.get();
            existingTrash.setName(trash.getName());
            existingTrash.setCoin(trash.getCoin());
            existingTrash.setCategory(trash.getCategory());
            existingTrash.setUpdatedAt(LocalDateTime.now());

            Trash updatedTrash = trashService.save(trash);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated trash", updatedTrash);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
        }
    }

    @DeleteMapping("/trash/delete/{id}")
    public StandardResponse<Void> deleteTrash(@PathVariable("id") String id) {
        Optional<Trash> existingTrash = trashService.findById(id);
        if (existingTrash.isPresent()) {
            trashService.delete(id);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted trash with id: " + id, null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Trash Not Found", null);
        }
    }
}
