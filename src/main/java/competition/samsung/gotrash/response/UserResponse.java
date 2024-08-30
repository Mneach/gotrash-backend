package competition.samsung.gotrash.response;

import competition.samsung.gotrash.entity.Group;
import competition.samsung.gotrash.entity.Trash;
import competition.samsung.gotrash.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String imageUrl;
    private BigInteger coin;
    private List<Trash> trashHistory;
    private Group group;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
