package competition.samsung.gotrash.factory;

import competition.samsung.gotrash.entity.User;
import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.response.UserResponse;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponseFactory {

    public static UserResponse createUserResponse(User user, Group group) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .coin(user.getCoin())
                .rating(user.getRating())
                .trashHistory(user.getTrashHistory())
                .group(group)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .version(user.getVersion())
                .build();
    }
}
