package competition.samsung.gotrash.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;

@Data
public class RewardDTO {

    private String id;
    private String name;
    private BigInteger coin;
    private String description;
    private String rewardCategoryId;
    private MultipartFile file;
}
