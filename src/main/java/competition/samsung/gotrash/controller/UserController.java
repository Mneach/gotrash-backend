package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Integer id){
        return userService.findById(id);
    }

    @PostMapping("/user/add")
    public User save(@RequestBody User user){
        return userService.save(user);
    }

    @PostMapping("/user/addGuest")
    public User saveGuest(){
        User user = new User(1,
                "dummy",
                "dummy123",
                "dummy@gmail.com",
                "http://example.com/images/dummy.png",
                BigInteger.valueOf(150),
                LocalDateTime.now(),
                LocalDateTime.now());

        return userService.save(user);
    }
}
