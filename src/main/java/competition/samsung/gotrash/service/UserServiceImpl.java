package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public String delete(Integer id) {
        userRepository.deleteById(id);
        return "Successfully delete user with id : " + id;
    }
}
