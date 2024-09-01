package competition.samsung.gotrash.controller;

import competition.samsung.gotrash.constant.ServiceName;
import competition.samsung.gotrash.dto.AddCoinDTO;
import competition.samsung.gotrash.dto.UserDTO;
import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.factory.UserResponseFactory;
import competition.samsung.gotrash.repository.GroupRepository;
import competition.samsung.gotrash.response.StandardResponse;
import competition.samsung.gotrash.response.UserResponse;
import competition.samsung.gotrash.service.*;
import competition.samsung.gotrash.utils.S3BucketUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static competition.samsung.gotrash.entity.User.SEQUENCE_NAME;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private S3Service s3Service;
    private TrashService trashService;
    private GroupService groupService;
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

                Optional<Group> group = groupService.findById(user.getGroupId());

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

            Optional<Group> group = groupService.findById(user.getGroupId());
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
        user.setUsername("dummy");
        user.setPassword("dummy123");
        user.setEmail("dummy@gmail.com");
        user.setImageName("");
        user.setGroupId("");
        user.setTrashHistory(new ArrayList<>());
        user.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTd-8kr6IGwu8T6y_Lc-0ZfAnGBFF4MvLjY-w&s");
        user.setCoin(BigInteger.valueOf(0));

        User data = userService.save(user);

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

    @PostMapping("/user/addCoin")
    public StandardResponse<User> addCoin(@RequestBody AddCoinDTO request){
        Optional<User> userOptional = userService.findById(request.getUserId());
        Optional<Trash> trashOptional = trashService.findFirstByCategory(request.getTrashCategory());

        if (userOptional.isPresent() && trashOptional.isPresent()) {
            User user = userOptional.get();
            Trash trash = trashOptional.get();

            List<Trash> updateTrashHistory = user.getTrashHistory();
            updateTrashHistory.add(trash);
            BigInteger updateCoin = user.getCoin().add(trash.getCoin());
            user.setCoin(updateCoin);
            user.setTrashHistory(updateTrashHistory);


            User data = userService.save(user);
            return new StandardResponse<>(HttpStatus.OK.value(), "User Coin Updated", data);
        }else{
            String message = userOptional.isEmpty() ? "User Not Found" : "Item Not Found";
            return new StandardResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
        }
    }
}
