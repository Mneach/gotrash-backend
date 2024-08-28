package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.GroupSetting;
import competition.samsung.gotrash.dto.AddMemberDTO;
import competition.samsung.gotrash.dto.GroupDTO;
import competition.samsung.gotrash.dto.RemoveMemberDTO;
import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.service.GroupService;
import competition.samsung.gotrash.service.RewardService;
import competition.samsung.gotrash.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final RewardService rewardService;

    @GetMapping("/groups")
    public StandardResponse<List<Group>> getAllGroups() {
        List<Group> groups = groupService.findAll();
        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved groups", groups);
    }

    @GetMapping("/group/{id}")
    public StandardResponse<Group> getGroupById(@PathVariable String id) {
        Optional<Group> group = Optional.ofNullable(groupService.findById(id));
        if (group.isPresent()) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved group", group.get());
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
        }
    }

    @PostMapping("/group/add")
    public StandardResponse<Group> createGroup(@RequestBody GroupDTO groupDTO) {

        Optional<Reward> reward = rewardService.findById(groupDTO.getRewardId());
        Optional<User> user = userService.findById(groupDTO.getAdminId());

        if(user.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }else if(reward.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
        }

        Group group = new Group();
        List<User> members = new ArrayList<>();
        members.add(user.get());

        group.setId(UUID.randomUUID().toString());
        group.setMembers(members);
        group.setGroupName(groupDTO.getGroupName());
        group.setTargetReward(reward.get());
        group.setCoins(0);
        group.setAdminId(groupDTO.getAdminId());

        Group savedGroup = groupService.save(group);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created group", savedGroup);
    }

    @DeleteMapping("/group/delete/{id}")
    public StandardResponse<Void> deleteGroup(@PathVariable String id) {
        Optional<Group> existingGroup = Optional.ofNullable(groupService.findById(id));
        if (existingGroup.isPresent()) {
            groupService.delete(id);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted group with id: " + id, null);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
        }
    }

    @PostMapping("/group/add-member")
    public StandardResponse<Group> addMemberToGroup(@RequestBody AddMemberDTO addMemberDTO) {

        Optional<User> user = userService.findById(addMemberDTO.getUserId());

        if(user.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

        Group updatedGroup = groupService.addMember(addMemberDTO.getGroupId(), user.get());
        if (updatedGroup != null) {
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully added member to group", updatedGroup);
        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
        }
    }

    @PostMapping("/group/remove-member")
    public StandardResponse<Group> removeMemberFromGroup(@RequestBody RemoveMemberDTO removeMemberDTO) {
        Optional<User> user = userService.findById(removeMemberDTO.getUserId());

        if(user.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

        Group updatedGroup = groupService.removeMember(removeMemberDTO.getGroupId(), user.get());
        if (updatedGroup != null) {
            if(updatedGroup.getMembers().isEmpty()){
                groupService.delete(updatedGroup.getId());
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully delete group", updatedGroup);
            }else{
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully removed member from group", updatedGroup);
            }

        } else {
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
        }
    }
}
