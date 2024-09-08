package competition.samsung.gotrash.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trash_sequences")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrashSequence {
    @Id
    private String id;
    private int number;
}
