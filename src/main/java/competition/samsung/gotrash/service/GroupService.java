package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> findAll();
    Optional<Group> findById(String id);
    Group save(Group group);
    String delete(String id);
}
