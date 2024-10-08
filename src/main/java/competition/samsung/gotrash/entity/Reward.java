package competition.samsung.gotrash.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Document(collection = "rewards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    @Id
    private String id;
    private String name;
    private BigInteger coin;
    private String description;
    private String imageName;
    private String imageUrl;
    private String rewardCategoryId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
