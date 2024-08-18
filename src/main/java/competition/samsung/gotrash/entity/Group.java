package competition.samsung.gotrash.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    private String id;
    private String groupName;
    private String groupImage;
    private int coins;
    private int targetReward;
    private List<User> members;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
