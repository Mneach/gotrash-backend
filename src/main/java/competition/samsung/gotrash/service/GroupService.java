package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> findAll() throws Exception;
    Optional<Group> findById(String id) throws Exception;
    Group save(Group group);
    String delete(String id);
    Optional<Group> getGroupByIdAndSortMembers(String id);
    List<Group> findGroupsByUserId(Integer id);
}
