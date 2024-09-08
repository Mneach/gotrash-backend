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

import java.util.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final RewardService rewardService;

    @GetMapping("/groups")
    public StandardResponse<List<Group>> getAllGroups() {
        try {
            List<Group> groups  = groupService.findAll();
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved groups", groups);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/group/{id}")
    public StandardResponse<Group> getGroupById(@PathVariable String id) {
        try {
            Optional<Group> group = groupService.findById(id);
            if (group.isPresent()) {
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved group", group.get());
            } else {
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
            }
        } catch (Exception e) {
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
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

        if(!Objects.equals(user.get().getGroupId(), "")){
            return new StandardResponse<>(HttpStatus.CONFLICT.value(), "you can only have 1 group", null);
        }

        Group group = new Group();
        String groupId = UUID.randomUUID().toString();

        // update user group id
        user.get().setGroupId(groupId);
        userService.save(user.get());

        List<User> members = new ArrayList<>();
        members.add(user.get());

        group.setId(groupId);
        group.setMembers(members);
        group.setGroupName(groupDTO.getGroupName());
        group.setTargetReward(reward.get());
        group.setCoins(0);
        group.setAdminId(groupDTO.getAdminId());

        groupService.save(group);
        return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully created group", null);
    }
    @PatchMapping("/group/update/{id}")
    public StandardResponse<Group> udpateGroup(@PathVariable String id, @RequestBody GroupDTO groupDTO) {

        try {
            Optional<Reward> reward = rewardService.findById(groupDTO.getRewardId());
            Optional<User> user = userService.findById(groupDTO.getAdminId());
            Optional<Group> group = groupService.findById(id);

            if(group.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
            } else if(user.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
            }else if(reward.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Reward Not Found", null);
            }

            Group ExistingGroup = group.get();

            ExistingGroup.setGroupName(groupDTO.getGroupName());
            ExistingGroup.setTargetReward(reward.get());
            ExistingGroup.setAdminId(groupDTO.getAdminId());

            groupService.save(ExistingGroup);
            return new StandardResponse<>(HttpStatus.CREATED.value(), "Successfully updated group", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @DeleteMapping("/group/delete/{id}")
    public StandardResponse<Void> deleteGroup(@PathVariable String id) {
        try {
            Optional<Group> existingGroup = groupService.findById(id);

            if (existingGroup.isPresent()) {

                for(User user : existingGroup.get().getMembers()){
                    user.setGroupId("");
                    userService.save(user);
                }

                groupService.delete(id);
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully deleted group with id: " + id, null);
            } else {
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/group/add-member")
    public StandardResponse<Group> addMemberToGroup(@RequestBody AddMemberDTO addMemberDTO) {


        try {
            Optional<User> user = userService.findById(addMemberDTO.getUserId());
            Optional<Group> group = groupService.findById(addMemberDTO.getGroupId());

            if(user.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
            }

            if(group.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
            }

            if(Objects.equals(user.get().getGroupId(), group.get().getId())){
                return new StandardResponse<>(HttpStatus.CONFLICT.value(), "User already join this group", null);
            }else if(!Objects.equals(user.get().getGroupId(), "")){
                return new StandardResponse<>(HttpStatus.CONFLICT.value(), "User already join another group", null);
            }

            // update user group id
            user.get().setGroupId(addMemberDTO.getGroupId());
            userService.save(user.get());

            // update group member
            group.get().getMembers().add(user.get());
            groupService.save(group.get());

            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully added member to group", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @PostMapping("/group/remove-member")
    public StandardResponse<Group> removeMemberFromGroup(@RequestBody RemoveMemberDTO removeMemberDTO) {

        try {
            Optional<User> user = userService.findById(removeMemberDTO.getUserId());
            Optional<Group> group = groupService.findById(removeMemberDTO.getGroupId());

            if(user.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
            }

            if(group.isEmpty()){
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "Group Not Found", null);
            }

            // update group member
            List<User> members  = group.get().getMembers();

            if(members.remove(user.get())){
                // update user group id
                user.get().setGroupId("");
                userService.save(user.get());
            }

            for(User temp : members){
                System.out.println(temp.getId());
            }

            group.get().setMembers(members);
            Group updatedGroup = groupService.save(group.get());

            if(updatedGroup.getMembers().isEmpty()){
                groupService.delete(updatedGroup.getId());
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully removed member and delete group", null);
            }else{
                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully removed member from group", null);
            }
        } catch (Exception e) {
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }
}
