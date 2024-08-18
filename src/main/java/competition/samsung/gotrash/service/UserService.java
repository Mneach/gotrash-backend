package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User save(User user);
    String delete(Integer id);
}
