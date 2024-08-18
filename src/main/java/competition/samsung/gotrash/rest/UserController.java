package competition.samsung.gotrash.rest;

import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/")
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping("/")
    public User save(@RequestBody User user){
        return userService.save(user);
    }
}
