package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.dto.AddCoinDTO;
import competition.samsung.gotrash.dto.MissionDTO;
import competition.samsung.gotrash.dto.UserDTO;
import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Notification;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.factory.UserResponseFactory;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.response.UserResponse;
import competition.samsung.gotrash.service.*;
import competition.samsung.gotrash.service.generator.SequenceGeneratorService;
import competition.samsung.gotrash.utils.S3BucketUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

import static competition.samsung.gotrash.entity.User.SEQUENCE_NAME;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private S3Service s3Service;
    private TrashService trashService;
    private GroupService groupService;
    private NotificationService notificationService;
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/users")
    public StandardResponse<List<UserResponse>> findAll(){
        try{
            List<UserResponse> userResponses = new ArrayList<>();

            List<User> users =  userService.findAll();
            for(User user : users){
                if(!Objects.equals(user.getImageName(), "")){
                    String objectKeys = S3BucketUtil.createObjectKey(ServiceName.USER, user.getId().toString(), user.getImageName());
                    String presignUrl = s3Service.getPresignUrl(objectKeys);
                    user.setImageUrl(presignUrl);
                }

                Optional<Group> group = groupService.getGroupByIdAndSortMembers(user.getGroupId());

                if(group.isPresent()){
                    userResponses.add(UserResponseFactory.createUserResponse(user, group.get()));
                }else{
                    userResponses.add(UserResponseFactory.createUserResponse(user, null));
                }
            }
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully retrieved users", userResponses);
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/user/{id}")
    public StandardResponse<UserResponse> findById(@PathVariable Integer id){
        Optional<User> data = userService.findById(id);

        if(data.isEmpty()){
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

        try{
            User user = data.get();

            if(!Objects.equals(user.getImageName(), "")){
                String objectKeys = S3BucketUtil.createObjectKey(ServiceName.USER, user.getId().toString(), user.getImageName());
                String presignUrl = s3Service.getPresignUrl(objectKeys);
                user.setImageUrl(presignUrl);
            }

            Optional<Group> group = groupService.getGroupByIdAndSortMembers(user.getGroupId());
            UserResponse userResponses;

            if(group.isPresent()){
                userResponses = UserResponseFactory.createUserResponse(user, group.get());
            }else{
                userResponses = UserResponseFactory.createUserResponse(user, null);
            }

            return new StandardResponse<>(HttpStatus.OK.value(), "User retrieved successfully", userResponses);
        }catch(Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping(value = "/user/add", consumes = {"multipart/form-data"})
    public StandardResponse<User> save(@RequestBody UserDTO userDTO){
        Integer id = sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME);
        User user = new User();
        user.setId(id);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCoin(BigInteger.valueOf(0));
        user.setTrashHistory(new ArrayList<>());
        user.setGroupId("");
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRating(BigInteger.valueOf(0));

        try {
            String imageUrl = "";
            if(userDTO.getFile() != null && !userDTO.getFile().isEmpty()){
                imageUrl = s3Service.uploadFileAndGetUrl(userDTO.getFile(), ServiceName.USER, user.getId().toString());
                user.setImageName(userDTO.getFile().getOriginalFilename());
            }else if(userDTO.getFile() != null && userDTO.getFile().isEmpty()){
                user.setImageName("");
            }

            User savedUser = userService.save(user);
            savedUser.setImageUrl(imageUrl);
            return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created user", savedUser);
        } catch (Exception e) {
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/user/addGuest")
    public StandardResponse<User> saveGuest(){
        Integer id = sequenceGeneratorService.getSequenceNumber(SEQUENCE_NAME);
        User user = new User();
        user.setId(id);
        user.setUsername("dummy-" + id);
        user.setPassword("dummy123");
        user.setEmail("dummy-" + id + "@gmail.com");
        user.setImageName("");
        user.setGroupId("");
        user.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8kr6IGwu8T6y_Lc-0ZfAnGBFF4MvLjY-w&s");
        user.setCoin(BigInteger.valueOf(0));
        user.setRating(BigInteger.valueOf(0));
        user.setTrashHistory(new ArrayList<>());
        user.setPhoneNumber("");

        User data = userService.save(user);

        // Create Notification
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setTitle("Selamat Datang di Aplikasi GoTrash");
        notification.setUserId(user.getId());
        notification.setDescription("");
        notificationService.save(notification);

        return new StandardResponse<>(HttpStatus.OK.value(), "Successfully created guest", data);
    }

    @PatchMapping(value = "/user/update/{id}", consumes = {"multipart/form-data"})
    public StandardResponse<User> update(@PathVariable("id") String id, @ModelAttribute UserDTO userDTO){
        Optional<User> data = userService.findById(userDTO.getId());

        if(data.isPresent()){
            User existingUser = data.get();
            existingUser.setUsername(userDTO.getUsername());
            existingUser.setPassword(userDTO.getPassword());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setUpdatedAt(LocalDateTime.now());
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());

            try {
                String imageUrl = "";
                if(userDTO.getFile() != null && !userDTO.getFile().isEmpty()){
                    imageUrl = s3Service.uploadFileAndGetUrl(userDTO.getFile(), ServiceName.USER, userDTO.getId().toString());
                    existingUser.setImageName(userDTO.getFile().getOriginalFilename());
                }else if(userDTO.getFile() != null && userDTO.getFile().isEmpty()){
                    existingUser.setImageName("");
                }else if(userDTO.getFile() == null && !Objects.equals(existingUser.getImageName(), "")){
                    String objectKeys = S3BucketUtil.createObjectKey(ServiceName.USER, existingUser.getId().toString(), existingUser.getImageName());
                    imageUrl = s3Service.getPresignUrl(objectKeys);
                }

                User udpatedUser = userService.save(existingUser);
                udpatedUser.setImageUrl(imageUrl);

                return new StandardResponse<>(HttpStatus.OK.value(), "Successfully updated user", udpatedUser);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }
        }else{
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }

    }

    @DeleteMapping("/user/delete/{id}")
    public StandardResponse<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userService.delete(user.getId());

            try {
                if (!Objects.equals(user.getImageName(), "")) {
                    String objectKey = ServiceName.USER + "/" + user.getId() + "/";
                    s3Service.deleteFolder(objectKey);
                }
                return new StandardResponse<>(HttpStatus.OK.value(), "User successfully deleted", null);
            } catch (Exception e) {
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }
        }

        return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
    }

    @PostMapping("/user/coin")
    public StandardResponse<Trash> addCoin(@RequestBody AddCoinDTO request){
        try{
            Optional<User> userOptional = userService.findById(request.getUserId());
            Optional<Trash> trashOptional = trashService.findById(request.getTrashId());

            if (userOptional.isPresent() && trashOptional.isPresent()) {
                try{
                    User user = userOptional.get();
                    Trash trash = trashOptional.get();
                    System.out.println("TESTER");
                    List<Trash> updateTrashHistory = user.getTrashHistory();
                    updateTrashHistory.add(trash);
                    BigInteger updateCoin = user.getCoin().add(trash.getCoin());
                    BigInteger updateRating = user.getRating().add(trash.getRating());
                    user.setCoin(updateCoin);
                    user.setTrashHistory(updateTrashHistory);
                    user.setRating(updateRating);

                    Optional<Group> optionalGroup = groupService.findById(user.getGroupId());

                    if(optionalGroup.isPresent()){
                        Group group = optionalGroup.get();

                        boolean updated = false;
                        for(User member : group.getMembers()){
                            if(Objects.equals(member.getId(), user.getId())){
                                member.setRating(user.getRating());
                                member.setCoin(user.getCoin());
                                updated = true;
                            }
                        }

                        if(updated){
                            groupService.save(group);
                        }
                    }

                    User data = userService.save(user);
                    return new StandardResponse<>(HttpStatus.OK.value(), "User Coin Updated", trash);
                }catch (Exception e){
                    return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
                }
            }else{
                String message = userOptional.isEmpty() ? "User Not Found" : "Item Not Found";
                return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
            }
        }catch (Exception e){
            return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/user/mission/coin")
    public StandardResponse<User> addMissionCoin(@RequestBody MissionDTO missionDTO){
        Optional<User> userOptional = userService.findById(missionDTO.getUserId());

        if (userOptional.isPresent()) {
            try{
                User user = userOptional.get();
                BigInteger updateCoin = user.getCoin().add(missionDTO.getCoin());
                user.setCoin(updateCoin);

                Optional<Group> optionalGroup = groupService.findById(user.getGroupId());

                if(optionalGroup.isPresent()){
                    Group group = optionalGroup.get();

                    boolean updated = false;
                    for(User member : group.getMembers()){
                        if(Objects.equals(member.getId(), user.getId())){
                            member.setRating(user.getRating());
                            member.setCoin(user.getCoin());
                            updated = true;
                        }
                    }

                    if(updated){
                        groupService.save(group);
                    }
                }

                User data = userService.save(user);
                return new StandardResponse<>(HttpStatus.OK.value(), "User Coin Updated", data);
            }catch (Exception e){
                return new StandardResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            }
        }else{
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), "User Not Found", null);
        }
    }
}
