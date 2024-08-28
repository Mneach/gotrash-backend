package competition.samsung.gotrash.service;

import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService{

    private GroupRepository groupRepository;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> findById(String id) {
        return groupRepository.findById(id);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public String delete(String id) {
        groupRepository.deleteById(id);
        return "Successfully delete group with id " + id;
    }

    @Override
    public Group addMember(String groupId, User user) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.getMembers().add(user);
            return groupRepository.save(group);
        }
        return null;
    }

    @Override
    public Group removeMember(String groupId, User user) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.getMembers().remove(user);
            return groupRepository.save(group);
        }
        return null;
    }
}
