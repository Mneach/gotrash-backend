package competition.samsung.gotrash.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private MultipartFile file;
    private BigInteger coin;
}
