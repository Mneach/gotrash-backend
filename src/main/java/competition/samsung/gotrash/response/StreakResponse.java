package competition.samsung.gotrash.response;

import competition.samsung.gotrash.entity.Trash;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreakResponse {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Trash> trashHistory;
}
