package competition.samsung.gotrash.dto;

import competition.samsung.gotrash.entity.Reward;
import competition.samsung.gotrash.entity.User;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class GroupDTO {
    private String groupName;
    private String rewardId;
    private Integer adminId;
}
