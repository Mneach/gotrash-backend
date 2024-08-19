package competition.samsung.gotrash.response;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class StandardResponse<T> {

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private T data;

    public StandardResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

}
