package competition.samsung.gotrash.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TrashBinDTO {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private MultipartFile file;
}
