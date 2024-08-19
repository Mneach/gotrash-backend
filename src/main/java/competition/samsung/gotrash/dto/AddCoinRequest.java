package competition.samsung.gotrash.dto;

import lombok.Data;

@Data
public class AddCoinRequest {
    private int userId;
    private String trashId;
}
