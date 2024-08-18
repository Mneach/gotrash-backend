package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.User;

import java.util.List;

public interface GroupService {
    List<Group> findAll();
    Group findById(String id);
    Group save(Group group);
    String delete(String id);
    Group addMember(String groupId, User user);
    Group removeMember(String groupId, User user);
}
