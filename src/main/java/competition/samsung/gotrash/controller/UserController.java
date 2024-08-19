package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.dto.AddCoinRequest;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.TrashService;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private TrashService trashService;

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
        User data = userService.save(user);
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created user", data);
    }

    @PostMapping("/user/addGuest")
    public StandardResponse<User> saveGuest(){
        User user = new User(1,
                "dummy",
                "dummy123",
                "dummy@gmail.com",
                "http://example.com/images/dummy.png",
                BigInteger.valueOf(150),
                LocalDateTime.now(),
                LocalDateTime.now());

        User data = userService.save(user);

        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created guest", data);
    }

    @PutMapping("/user/update/{id}")
    public StandardResponse<User> update(@PathVariable("id") String id, @RequestBody User user){
        Optional<User> data = userService.findById(user.getId());

        if(data.isPresent()){
            User existingUser = data.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setEmail(user.getEmail());
            existingUser.setProfileImage(user.getProfileImage());
            existingUser.setCoins(user.getCoins());
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

            BigInteger updatedCoins = user.getCoins().add(trash.getCoin());
            user.setCoins(updatedCoins);

            User data = userService.save(user);
            return new StandardResponse<>(HttpStatus.OK.value(), "User Coin Updated", data);
        }else{
            String message = userOptional.isEmpty() ? "User Not Found" : "Item Not Found";
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
        }
    }
}
