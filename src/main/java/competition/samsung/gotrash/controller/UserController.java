package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.dto.AddCoinRequest;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.SequenceGeneratorService;
import competition.samsung.gotrash.service.TrashService;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static competition.samsung.gotrash.entity.User.SEQUENCE_NAME;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private TrashService trashService;
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/users")
    public StandardResponse<List<User>> findAll(){

        List<User> users =  userService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved users", users);
    }

    @GetMapping("/user/{id}")
    public StandardResponse<User> findById(@PathVariable Integer id){
        Optional<User> data = userService.findById(id);

        if(data.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

        return new StandardResponse<>(HttpStatus.OK.value(), "User retrieved successfully", data.get());
    }

    @PostMapping("/user/add")
    public StandardResponse<User> save(@RequestBody User user){
        Integer id = sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME);
        user.setId(id);

        User data = userService.save(user);
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created user", data);
    }

    @PostMapping("/user/addGuest")
    public StandardResponse<User> saveGuest(){
        Integer id = sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME);
        User user = new User(id,
                "dummy",
                "dummy123",
                "dummy@gmail.com",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8kr6IGwu8T6y_Lc-0ZfAnGBFF4MvLjY-w&s",
                BigInteger.valueOf(150),
                LocalDateTime.now(),
                LocalDateTime.now());

        User data = userService.save(user);

        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created guest", data);
    }

    @PatchMapping("/user/update/{id}")
    public StandardResponse<User> update(@PathVariable("id") String id, @RequestBody User user){
        Optional<User> data = userService.findById(user.getId());

        if(data.isPresent()){
            User existingUser = data.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            existingUser.setProfileImage(user.getProfileImage());
            existingUser.setCoin(user.getCoin());
            existingUser.setUpdatedAt(LocalDateTime.now());

            User updatedUser  = userService.save(existingUser);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated user", updatedUser);
        }else{
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

    }

    @DeleteMapping("/user/delete/{id}")
    public StandardResponse<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            userService.delete(id);
            return new StandardResponse<>(HttpStatus.NO_CONTENT.value(), "User successfully deleted", null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
        }
    }

    @PostMapping("/user/addCoin")
    public StandardResponse<User> addCoin(@RequestBody AddCoinRequest request){
        Optional<User> userOptional = userService.findById(request.getUserId());
        Optional<Trash> trashOptional = trashService.findById(request.getTrashId());

        if (userOptional.isPresent() && trashOptional.isPresent()) {
            User user = userOptional.get();
            Trash trash = trashOptional.get();

            BigInteger updateCoin = user.getCoin().add(trash.getCoin());
            user.setCoin(updateCoin);

            User data = userService.save(user);
            return new StandardResponse<>(HttpStatus.OK.value(), "User Coin Updated", data);
        }else{
            String message = userOptional.isEmpty() ? "User Not Found" : "Item Not Found";
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
        }
    }
}
